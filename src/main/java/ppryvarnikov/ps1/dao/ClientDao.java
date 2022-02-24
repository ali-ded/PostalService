package ppryvarnikov.ps1.dao;

import ppryvarnikov.ps1.dto.Client;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDao implements Dao<Client, Long> {
    private final Logger logger = Logger.getLogger(ClientDao.class.getName());

    @Override
    public Optional<Client> get(Long id) {
        Optional<Client> optionalClient = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.clients where id = " + id);
            Client client = null;

            if (resultSet.next()) {
                client = createClientFromResultSet(resultSet);
            }

            optionalClient = Optional.ofNullable(client);
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalClient;
    }

    private Client createClientFromResultSet(ResultSet resultSet) throws SQLException {
        return new Client(
                resultSet.getLong("id"),
                resultSet.getString("surname"),
                resultSet.getString("first_name"),
                resultSet.getString("patronymic"),
                resultSet.getString("email"),
                resultSet.getLong("phone_number")
        );
    }

    @Override
    public Set<Client> getAll() {
        Set<Client> clients = new HashSet<>();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.clients");

            while (resultSet.next()) {
                clients.add(createClientFromResultSet(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return clients;
    }

    @Override
    public boolean insert(Client client) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into postal_service.clients" +
                            "(surname, first_name, patronymic, email, phone_number) " +
                            "values(?, ?, ?, ?, ?)");
            createPreparedStatementFromClient(preparedStatement, client);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                result = true;
                logger.log(Level.INFO, "The new client (" + client.getSurname() + " " + client.getFirstName() + ")" +
                        " has been successfully added to the 'clients' table.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    @Override
    public boolean update(Client client) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update postal_service.clients set " +
                            "surname=?, first_name=?, patronymic=?, email=?, phone_number=? " +
                            "where id=?");
            createPreparedStatementFromClient(preparedStatement, client);
            preparedStatement.setLong(6, client.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                result = true;
                logger.log(Level.INFO, "The client (id=" + client.getId() + ")" +
                        " information was successfully updated in the 'clients' table.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    private void createPreparedStatementFromClient(PreparedStatement preparedStatement, Client client) throws SQLException {
        preparedStatement.setString(1, client.getSurname());
        preparedStatement.setString(2, client.getFirstName());
        preparedStatement.setString(3, client.getPatronymic());
        preparedStatement.setString(4, client.getEmail());
        preparedStatement.setLong(5, client.getPhoneNumber());
    }

    @Override
    public boolean delete(Long id) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            int rowsDeleted = statement.executeUpdate("delete from postal_service.clients " +
                    "where id=" + id);
            if (rowsDeleted == 1) {
                result = true;
                logger.log(Level.INFO, "The client (id=" + id + ") was successfully removed from the 'clients' table.");
            }
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    public Optional<Client> getClientByPhone(Long phoneNumber) {
        Optional<Client> optionalClient = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from postal_service.clients where phone_number=?"
            );
            preparedStatement.setLong(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            Client client = null;
            if (resultSet.next()) {
                client = createClientFromResultSet(resultSet);
            }
            optionalClient = Optional.ofNullable(client);
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalClient;
    }
}
