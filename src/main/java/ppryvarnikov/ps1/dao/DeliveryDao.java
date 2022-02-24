package ppryvarnikov.ps1.dao;

import ppryvarnikov.ps1.dto.Delivery;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryDao implements Dao<Delivery, Long>{
    private final Logger logger = Logger.getLogger(DeliveryDao.class.getName());

    @Override
    public Optional<Delivery> get(Long id) {
        Optional<Delivery> optionalShipped = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from postal_service.deliveries where id = " + id);
            if (resultSet.next()) {
                Delivery delivery = createDeliveryFromResultSet(resultSet);
                optionalShipped = Optional.of(delivery);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalShipped;
    }

    private Delivery createDeliveryFromResultSet(ResultSet resultSet) throws SQLException {
        return new Delivery(
                resultSet.getLong("id"),
                new ClientDao().get(resultSet.getLong("id_client")).orElseThrow(),
                new DepartmentDao().get(resultSet.getInt("id_department_sender")).orElseThrow(),
                new DepartmentDao().get(resultSet.getInt("id_department_recipient")).orElseThrow(),
                resultSet.getLong("recipient_phone"),
                resultSet.getString("recipient_surname"),
                resultSet.getString("recipient_first_name"),
                resultSet.getString("recipient_patronymic"),
                new ParcelStatusDao().get(resultSet.getShort("id_parcel_status")).orElseThrow(),
                resultSet.getTimestamp("date_time_creation"),
                resultSet.getTimestamp("date_time_status_change")
        );
    }

    @Override
    public Set<Delivery> getAll() {
        Set<Delivery> deliveries = new HashSet<>();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.deliveries");
            while (resultSet.next()) {
                deliveries.add(createDeliveryFromResultSet(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return deliveries;
    }

    @Override
    public boolean insert(Delivery delivery) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into postal_service.deliveries" +
                            "(id_client, id_department_sender, id_department_recipient, " +
                            "recipient_phone, recipient_surname, recipient_first_name, " +
                            "recipient_patronymic) " +
                            "values(?, ?, ?, ?, ?, ?, ?)");
            fillPreparedStatementForMandatoryFields(preparedStatement, delivery);
            preparedStatement.setString(7, delivery.getPatronymicRecipient());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                result = true;
                logger.log(Level.INFO, "The new delivery has been successfully added to the 'deliveries' table.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error.", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Unsuccessful attempt to add a new delivery to the database.");
        }
        finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    private void fillPreparedStatementForMandatoryFields(PreparedStatement ps,
                                                         Delivery delivery) throws SQLException, NullPointerException {
        ps.setLong(1, delivery.getSender().getId());
        ps.setInt(2, delivery.getDepartmentSender().getId());
        ps.setInt(3, delivery.getDepartmentRecipient().getId());
        ps.setLong(4, delivery.getPhoneRecipient());
        ps.setString(5, delivery.getSurnameRecipient());
        ps.setString(6, delivery.getFirstNameRecipient());
    }

    @Override
    public boolean update(Delivery delivery) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update postal_service.deliveries set " +
                            "id_client=?, id_department_sender=?, id_department_recipient=?, " +
                            "recipient_phone=?, recipient_surname=?, recipient_first_name=?, " +
                            "recipient_patronymic=?, id_parcel_status=?, date_time_creation=?, " +
                            "date_time_status_change=? where id=?");
            fillPreparedStatementForMandatoryFields(preparedStatement, delivery);
            preparedStatement.setString(7, delivery.getPatronymicRecipient());
            preparedStatement.setShort(8, delivery.getParcelStatus().getId());
            preparedStatement.setTimestamp(9, delivery.getDateTimeCreation());
            preparedStatement.setTimestamp(10, delivery.getDateTimeStatusChange());
            preparedStatement.setLong(11, delivery.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                result = true;
                logger.log(Level.INFO, "The delivery (id=" + delivery.getId() + ")" +
                        " information was successfully updated in the 'deliveries' table.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Cannot update field (id=" + delivery.getId() +
                    ") in 'deliveries' table.");
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    @Override
    public boolean delete(Long id) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            int rowsDeleted = statement.executeUpdate("delete from postal_service.deliveries " +
                    "where id=" + id);
            if (rowsDeleted == 1) {
                result = true;
                logger.log(Level.INFO, "The delivery (id=" + id + ")" +
                        " was successfully removed from the 'deliveries' table.");
            }
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    public int getNumberOfRows() {
        int numberOfRows = 0;
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select count(*) from postal_service.deliveries");
            if (resultSet.next()) {
                numberOfRows = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return numberOfRows;
    }
}
