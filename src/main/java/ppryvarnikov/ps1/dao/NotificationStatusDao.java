package ppryvarnikov.ps1.dao;

import ppryvarnikov.ps1.dto.NotificationStatus;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationStatusDao implements Dao<NotificationStatus, Short> {
    private final Logger logger = Logger.getLogger(NotificationStatusDao.class.getName());

    @Override
    public Optional<NotificationStatus> get(Short id) {
        Optional<NotificationStatus> optionalNotificationStatus = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from postal_service.notification_statuses where id = " + id);
            NotificationStatus notificationStatus = null;
            if (resultSet.next()) {
                notificationStatus = createNotificationStatusFromResultSet(resultSet);
            }
            optionalNotificationStatus = Optional.ofNullable(notificationStatus);
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalNotificationStatus;
    }

    private NotificationStatus createNotificationStatusFromResultSet(ResultSet resultSet) throws SQLException {
        return new NotificationStatus(
                resultSet.getShort("id"),
                resultSet.getString("status")
        );
    }

    @Override
    public Set<NotificationStatus> getAll() {
        Set<NotificationStatus> notificationStatuses = new HashSet<>();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.notification_statuses");
            while (resultSet.next()) {
                notificationStatuses.add(createNotificationStatusFromResultSet(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return notificationStatuses;
    }

    @Override
    public boolean insert(NotificationStatus notificationStatus) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into postal_service.notification_statuses(status) values(?)");
            preparedStatement.setString(1, notificationStatus.getStatus());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                result = true;
                logger.log(Level.INFO, "The new notification status (" + notificationStatus.getStatus() +
                        ") has been successfully added to the 'notification_statuses' table.");
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
    public boolean update(NotificationStatus notificationStatus) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update postal_service.notification_statuses set status=? where id=?");
            preparedStatement.setString(1, notificationStatus.getStatus());
            preparedStatement.setInt(2, notificationStatus.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                result = true;
                logger.log(Level.INFO, "The notification status (id=" + notificationStatus.getId() + ")" +
                        " information was successfully updated in the 'notification_statuses' table.");
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
    public boolean delete(Short id) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            int rowsDeleted = statement.executeUpdate("delete from postal_service.notification_statuses " +
                    "where id=" + id);
            if (rowsDeleted == 1) {
                result = true;
                logger.log(Level.INFO, "The notification status (id=" + id + ")" +
                        " was successfully removed from the 'notification_statuses' table.");
            }
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }
}
