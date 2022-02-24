package ppryvarnikov.ps1.io;

import ppryvarnikov.ps1.dao.*;
import ppryvarnikov.ps1.dto.*;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriterTablesToFile {
    private final Logger logger = Logger.getLogger(WriterTablesToFile.class.getName());

    public void writeTableClientsToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-20s|%3$-15s|%4$-15s|%5$-25s|%6$-15s|\n";
            String separator = "-".repeat(107);
            file.println("\nTable 'clients'");
            file.println(separator);
            file.printf(format, "id", "surname", "first_name", "patronymic", "email", "phone_number");
            file.println(separator);
            for (Client client : new ClientDao().getAll()) {
                file.printf(format,
                        client.getId(),
                        client.getSurname(),
                        client.getFirstName(),
                        client.getPatronymic(),
                        client.getEmail(),
                        client.getPhoneNumber());
            }
            file.println(separator);
            logger.log(Level.INFO, "'Clients' table data was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing 'clients' table to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeTableDeliveriesToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-10s|%3$-20s|%4$-23s|%5$-15s|%6$-20s|%7$-20s|%8$-20s|%9$-16s|%10$-26s|%11$-26s|\n";
            String separator = "-".repeat(218);
            file.println("\nTable 'deliveries'");
            file.println(separator);
            file.printf(format,
                    "id",
                    "id_client",
                    "id_department_sender",
                    "id_department_recipient",
                    "recipient_phone",
                    "recipient_surname",
                    "recipient_first_name",
                    "recipient_patronymic",
                    "id_parcel_status",
                    "date_time_creation",
                    "date_time_status_change");
            file.println(separator);
            for (Delivery delivery : new DeliveryDao().getAll()) {
                file.printf(format,
                        delivery.getId(),
                        delivery.getSender().getId(),
                        delivery.getDepartmentSender().getId(),
                        delivery.getDepartmentRecipient().getId(),
                        delivery.getPhoneRecipient(),
                        delivery.getSurnameRecipient(),
                        delivery.getFirstNameRecipient(),
                        delivery.getPatronymicRecipient(),
                        delivery.getParcelStatus().getId(),
                        delivery.getDateTimeCreation(),
                        delivery.getDateTimeStatusChange());
            }
            file.println(separator);
            logger.log(Level.INFO, "'Deliveries' table data was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing 'deliveries' table to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeTableDepartmentsToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-50s|\n";
            String separator = "-".repeat(63);
            file.println("\nTable 'departments'");
            file.println(separator);
            file.printf(format, "id", "description");
            file.println(separator);
            for (Department department : new DepartmentDao().getAll()) {
                file.printf(format,
                        department.getId(),
                        department.getDescription());
            }
            file.println(separator);
            logger.log(Level.INFO, "'Departments' table data was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing 'departments' table to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeTableNotificationsToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-100s|%3$-10s|\n";
            String separator = "-".repeat(124);
            file.println("\nTable 'notifications'");
            file.println(separator);
            file.printf(format, "id", "message", "id_status");
            file.println(separator);
            for (Notification notification : new NotificationDao().getAll()) {
                file.printf(format,
                        notification.getId(),
                        notification.getMessage(),
                        notification.getNotificationStatus().getId());
            }
            file.println(separator);
            logger.log(Level.INFO, "'Notifications' table data was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing 'notifications' table to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeTableNotificationStatusesToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-30s|\n";
            String separator = "-".repeat(43);
            file.println("\nTable 'notification_statuses'");
            file.println(separator);
            file.printf(format, "id", "status");
            file.println(separator);
            for (NotificationStatus notificationStatus : new NotificationStatusDao().getAll()) {
                file.printf(format,
                        notificationStatus.getId(),
                        notificationStatus.getStatus());
            }
            file.println(separator);
            logger.log(Level.INFO, "'Notification_statuses' table data was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing 'notification_statuses' table to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeTableParcelStatusesToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-30s|\n";
            String separator = "-".repeat(43);
            file.println("\nTable 'parcel_statuses'");
            file.println(separator);
            file.printf(format, "id", "status");
            file.println(separator);
            for (ParcelStatus parcelStatus : new ParcelStatusDao().getAll()) {
                file.printf(format,
                        parcelStatus.getId(),
                        parcelStatus.getStatus());
            }
            file.println(separator);
            logger.log(Level.INFO, "'Parcel_statuses' table data was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing 'parcel_statuses' table to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeJoinedTableNotificationsToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-100s|%3$-20s|\n";
            String separator = "-".repeat(134);
            file.println("\nTable 'notifications' joined with 'notification_statuses'");
            file.println(separator);
            file.printf(format, "id", "message", "status");
            file.println(separator);
            for (Notification notification : new NotificationDao().getAll()) {
                file.printf(format,
                        notification.getId(),
                        notification.getMessage(),
                        notification.getNotificationStatus().getStatus());
            }
            file.println(separator);
            logger.log(Level.INFO, "'Notifications' table data joined with 'notification_statuses' " +
                    "was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing 'notifications' table joined with " +
                    "'notification_statuses' to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeJoinedTableDeliveriesToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        PrintStream file = null;
        try {
            file = new PrintStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName, isAppendToTheEndOfTheFile)
                    ));
            String format = "|%1$-10s|%2$-15s|%3$-17s|%4$-17s|%5$-20s|%6$-19s|%7$-20s|%8$-23s|%9$-15s|" +
                    "%10$-20s|%11$-20s|%12$-20s|%13$-13s|%14$-26s|%15$-26s|\n";
            String separator = "-".repeat(297);
            file.println("\nTable 'deliveries' joined with 'clients' and 'departments' and 'parcel_statuses'");
            file.println(separator);
            file.printf(format,
                    "id",
                    "sender_surname",
                    "sender_first_name",
                    "sender_patronymic",
                    "sender_email",
                    "sender_phone_number",
                    "department_sender",
                    "department_recipient",
                    "recipient_phone",
                    "recipient_surname",
                    "recipient_first_name",
                    "recipient_patronymic",
                    "parcel_status",
                    "date_time_creation",
                    "date_time_status_change");
            file.println(separator);
            for (Delivery delivery : new DeliveryDao().getAll()) {
                file.printf(format,
                        delivery.getId(),
                        delivery.getSender().getSurname(),
                        delivery.getSender().getFirstName(),
                        delivery.getSender().getPatronymic(),
                        delivery.getSender().getEmail(),
                        delivery.getSender().getPhoneNumber(),
                        delivery.getDepartmentSender().getDescription(),
                        delivery.getDepartmentRecipient().getDescription(),
                        delivery.getPhoneRecipient(),
                        delivery.getSurnameRecipient(),
                        delivery.getFirstNameRecipient(),
                        delivery.getPatronymicRecipient(),
                        delivery.getParcelStatus().getStatus(),
                        delivery.getDateTimeCreation(),
                        delivery.getDateTimeStatusChange());
            }
            file.println(separator);
            logger.log(Level.INFO, "Table 'deliveries' joined with 'clients' and 'departments' and " +
                    "'parcel_statuses' was successfully written to " + fileName + " file.");
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Error writing table 'deliveries' joined with 'clients' and " +
                    "'departments' and 'parcel_statuses' to file " + fileName, e);
        } finally {
            assert file != null;
            file.close();
        }
    }

    public void writeAllTablesToFile(String fileName, boolean isAppendToTheEndOfTheFile) {
        writeTableClientsToFile(fileName, isAppendToTheEndOfTheFile);
        writeTableDepartmentsToFile(fileName, true);
        writeTableParcelStatusesToFile(fileName, true);
        writeTableDeliveriesToFile(fileName, true);
        writeTableNotificationsToFile(fileName, true);
        writeTableNotificationStatusesToFile(fileName, true);
        writeJoinedTableDeliveriesToFile(fileName, true);
        writeJoinedTableNotificationsToFile(fileName, true);
    }
}
