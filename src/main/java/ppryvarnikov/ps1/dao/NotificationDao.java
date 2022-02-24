package ppryvarnikov.ps1.dao;

import ppryvarnikov.ps1.dto.Notification;

import java.sql.*;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationDao implements Dao<Notification, Long> {
    private final Logger logger = Logger.getLogger(NotificationDao.class.getName());

    @Override
    public Optional<Notification> get(Long id) {
        Optional<Notification> optionalNotification = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from postal_service.notifications where id = " + id);
            if (resultSet.next()) {
                Notification notification = new Notification();
                notification.setId(resultSet.getLong("id"));
                notification.setMessage(resultSet.getString("message"));
                notification.setNotificationStatus(
                        new NotificationStatusDao().get(
                                resultSet.getShort("id_status")).orElseThrow()
                );
                optionalNotification = Optional.of(notification);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalNotification;
    }

    @Override
    public Set<Notification> getAll() {
        Set<Notification> notifications = new HashSet<>();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.notifications");
            while (resultSet.next()) {
                notifications.add(
                        new Notification(
                                resultSet.getLong("id"),
                                resultSet.getString("message"),
                                new NotificationStatusDao().get(
                                        resultSet.getShort("id_status")
                                ).orElseThrow()
                        )
                );
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return notifications;
    }

    @Override
    public boolean insert(Notification notification) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into postal_service.notifications(message) values(?)");
            Optional<String> optionalMessage = Optional.ofNullable(notification.getMessage());
            preparedStatement.setString(1, optionalMessage.orElseThrow());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                result = true;
                logger.log(Level.INFO, "The new notification (" + notification.getMessage() +
                        ") has been successfully added to the 'notifications' table.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } catch (NoSuchElementException e) {
            logger.log(Level.WARNING, "Trying to insert an empty message into the notifications table.");
        }
        finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    @Override
    public boolean update(Notification notification) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update postal_service.notifications set message=?, id_status=? where id=?");
            preparedStatement.setString(1, notification.getMessage());
            preparedStatement.setShort(2, notification.getNotificationStatus().getId());
            preparedStatement.setLong(3, notification.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                result = true;
                logger.log(Level.INFO, "The notification (id=" + notification.getId() + ")" +
                        " information was successfully updated in the 'notifications' table.");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Cannot update field (id=" + notification.getId() +
                    ") in 'notifications' table because id_status field is empty.");
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
            int rowsDeleted = statement.executeUpdate("delete from postal_service.notifications " +
                    "where id=" + id);
            if (rowsDeleted == 1) {
                result = true;
                logger.log(Level.INFO, "The notification (id=" + id + ")" +
                        " was successfully removed from the 'notifications' table.");
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
                    "select count(*) from postal_service.notifications");
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
