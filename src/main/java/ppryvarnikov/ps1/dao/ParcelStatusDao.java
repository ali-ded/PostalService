package ppryvarnikov.ps1.dao;

import ppryvarnikov.ps1.dto.ParcelStatus;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParcelStatusDao implements Dao<ParcelStatus, Short> {
    private final Logger logger = Logger.getLogger(ParcelStatusDao.class.getName());

    @Override
    public Optional<ParcelStatus> get(Short id) {
        Optional<ParcelStatus> optionalParcelStatus = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from postal_service.parcel_statuses where id = " + id);
            ParcelStatus parcelStatus = null;
            if (resultSet.next()) {
                parcelStatus = createParcelStatusFromResultSet(resultSet);
            }
            optionalParcelStatus = Optional.ofNullable(parcelStatus);
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalParcelStatus;
    }

    private ParcelStatus createParcelStatusFromResultSet(ResultSet resultSet) throws SQLException {
        return new ParcelStatus(
                resultSet.getShort("id"),
                resultSet.getString("status")
        );
    }

    @Override
    public Set<ParcelStatus> getAll() {
        Set<ParcelStatus> parcelStatuses = new HashSet<>();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.parcel_statuses");
            while (resultSet.next()) {
                parcelStatuses.add(createParcelStatusFromResultSet(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return parcelStatuses;
    }

    @Override
    public boolean insert(ParcelStatus parcelStatus) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into postal_service.parcel_statuses(status) values(?)");
            preparedStatement.setString(1, parcelStatus.getStatus());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                result = true;
                logger.log(Level.INFO, "The new parcel status (" + parcelStatus.getStatus() +
                        ") has been successfully added to the 'parcel_statuses' table.");
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
    public boolean update(ParcelStatus parcelStatus) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update postal_service.parcel_statuses set status=? where id=?");
            preparedStatement.setString(1, parcelStatus.getStatus());
            preparedStatement.setInt(2, parcelStatus.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                result = true;
                logger.log(Level.INFO, "The parcel status (id=" + parcelStatus.getId() + ")" +
                        " information was successfully updated in the 'parcel_statuses' table.");
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
            int rowsDeleted = statement.executeUpdate("delete from postal_service.parcel_statuses " +
                    "where id=" + id);
            if (rowsDeleted == 1) {
                result = true;
                logger.log(Level.INFO, "The parcel status (id=" + id + ")" +
                        " was successfully removed from the 'parcel_statuses' table.");
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
