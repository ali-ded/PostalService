package ppryvarnikov.ps1.service;

import ppryvarnikov.ps1.dao.ClientDao;
import ppryvarnikov.ps1.dao.ConnectionPool;
import ppryvarnikov.ps1.dao.DeliveryDao;
import ppryvarnikov.ps1.dao.DepartmentDao;
import ppryvarnikov.ps1.dto.Client;
import ppryvarnikov.ps1.dto.Delivery;
import ppryvarnikov.ps1.dto.Department;
import ppryvarnikov.ps1.io.Constants;
import ppryvarnikov.ps1.io.Reader;
import ppryvarnikov.ps1.io.Writer;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostalService {
    private final Logger logger = Logger.getLogger(PostalService.class.getName());
    private Client authorizedClient;

    enum Commands {
        CREATE_TABLES,
        REGISTER_CLIENT,
        LOGIN,
        CREATE_DEPARTMENT,
        CREATE_DELIVERY
    }

    public void start() {
        Reader reader = new Reader();
        List<String> commandsFromFile = reader.getStringList(Constants.DIR, Constants.INPUT_FILE);
        String[] commandSplitted;

        try {
            new FileWriter(Constants.OUTPUT_FILE).close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to file " + Constants.OUTPUT_FILE, e);
        }

        for (String command : commandsFromFile) {
            commandSplitted = command.split("\\|");
            try {
                switch (Commands.valueOf(commandSplitted[0].replaceAll("\\s+", ""))) {
                    case CREATE_TABLES -> {
                        if (createTables()) {
                            new Writer().writeMesasgeToFile(command);
                        }
                    }
                    case REGISTER_CLIENT -> {
                        if (registerClient(Arrays.copyOfRange(commandSplitted, 1, commandSplitted.length))) {
                            new Writer().writeMesasgeToFile(command);
                        }
                    }
                    case LOGIN -> {
                        if (login(Arrays.copyOfRange(commandSplitted, 1, commandSplitted.length))) {
                            new Writer().writeMesasgeToFile(command);
                        }
                    }
                    case CREATE_DEPARTMENT -> {
                        if (createDepartment(Arrays.copyOfRange(commandSplitted, 1, commandSplitted.length))) {
                            new Writer().writeMesasgeToFile(command);
                        }
                    }
                    case CREATE_DELIVERY -> {
                        if (createDelivery(Arrays.copyOfRange(commandSplitted, 1, commandSplitted.length))) {
                            new Writer().writeMesasgeToFile(command);
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, "The " + commandSplitted[0] + " command does not exist.");
            }
        }
        new DeliveryHandler().start();
        new NotificationHandler().start();
    }

    private boolean createDelivery(String[] deliveryData) {
        if (deliveryData.length != 6) {
            logger.log(Level.SEVERE, "Error when trying to add a new delivery to the database. " +
                    "The CREATE_DELIVERY command must contain six fields.");
            return false;
        }

        long phoneRecipient;
        try {
            phoneRecipient = Long.parseLong(deliveryData[2]);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error when trying to add a new delivery to the database. " +
                    "The phone number recipient must contain only numbers.", e);
            return false;
        }

        if (authorizedClient == null) {
            logger.log(Level.SEVERE, "Error when trying to add a new delivery to the database. " +
                    "First you need to log in.");
            return false;
        }

        Delivery delivery = new Delivery();
        delivery.setSender(authorizedClient);
        try {
            delivery.setDepartmentSender(new DepartmentDao()
                    .getDepartmentByDescription(deliveryData[0])
                    .orElseThrow());
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "Department with description " + deliveryData[0] +
                    " was not found in the database.", e);
            return false;
        }
        try {
            delivery.setDepartmentRecipient(new DepartmentDao()
                    .getDepartmentByDescription(deliveryData[1])
                    .orElseThrow());
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "Department with description " + deliveryData[1] +
                    " was not found in the database.", e);
            return false;
        }
        delivery.setPhoneRecipient(phoneRecipient);
        delivery.setSurnameRecipient(deliveryData[3]);
        delivery.setFirstNameRecipient(deliveryData[4]);
        delivery.setPatronymicRecipient(deliveryData[5]);

        return new DeliveryDao().insert(delivery);
    }

    private boolean createDepartment(String[] departmentData) {
        if (departmentData.length != 1) {
            logger.log(Level.SEVERE, "Error when trying to add a new department to the database. " +
                    "The CREATE_DEPARTMENT command must contain only one field - description of department.");
            return false;
        }

        Department department = new Department(departmentData[0]);
        return new DepartmentDao().insert(department);
    }

    private boolean login(String[] clientLogin) {
        if (clientLogin.length != 1) {
            logger.log(Level.SEVERE, "Client authentication error. " +
                    "The LOGIN command must contain only one field - phone number.");
            return false;
        }

        long phoneNumber;
        try {
            phoneNumber = Long.parseLong(clientLogin[0]);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Client authentication error. " +
                    "The phone number must contain only numbers.", e);
            return false;
        }

        try {
            authorizedClient = new ClientDao().getClientByPhone(phoneNumber).orElseThrow();
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "Client with phone number " + phoneNumber +
                    " was not found in the database.", e);
            return false;
        }
        return true;
    }

    private boolean createTables() {
        boolean result = false;
        Reader reader = new Reader();
        String sqlQueries = reader.getString(Constants.DIR + Constants.TABLES);
        Connection connection = ConnectionPool.takeConnection();
        try {
            connection.createStatement().execute(sqlQueries);
            logger.log(Level.INFO, "Tables successfully created in database.");
            result = true;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error when trying to create tables.", e);
        }
        return result;
    }

    private boolean registerClient(String[] registrationData) {
        if (registrationData.length != 5) {
            logger.log(Level.SEVERE, "Error when registering a new client. " +
                    "Registration data must contain 5 fields.");
            return false;
        }

        long phoneNumber;
        try {
            phoneNumber = Long.parseLong(registrationData[4]);
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error when registering a new client. " +
                    "The phone number must contain only numbers.", e);
            return false;
        }

        Client client = new Client(registrationData[0],
                registrationData[1],
                registrationData[2],
                registrationData[3],
                phoneNumber);

        return new ClientDao().insert(client);
    }
}

