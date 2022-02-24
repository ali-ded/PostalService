package ppryvarnikov.ps1.dao;

import ppryvarnikov.ps1.dto.Department;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentDao implements Dao<Department, Integer> {
    private final Logger logger = Logger.getLogger(DepartmentDao.class.getName());

    @Override
    public Optional<Department> get(Integer id) {
        Optional<Department> optionalDepartment = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.departments where id = " + id);
            Department department = null;

            if (resultSet.next()) {
                department = createDepartmentFromResultSet(resultSet);
            }

            optionalDepartment = Optional.ofNullable(department);
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalDepartment;
    }

    private Department createDepartmentFromResultSet(ResultSet resultSet) throws SQLException {
        return new Department(
                resultSet.getInt("id"),
                resultSet.getString("description")
        );
    }

    @Override
    public Set<Department> getAll() {
        Set<Department> departments = new HashSet<>();
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from postal_service.departments");
            while (resultSet.next()) {
                departments.add(createDepartmentFromResultSet(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return departments;
    }

    @Override
    public boolean insert(Department department) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into postal_service.departments(description) values(?)");
            preparedStatement.setString(1, department.getDescription());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted == 1) {
                result = true;
                logger.log(Level.INFO, "The new department (" + department.getDescription() +
                        ") has been successfully added to the 'departments' table.");
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
    public boolean update(Department department) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update postal_service.departments set description=? where id=?");
            preparedStatement.setString(1, department.getDescription());
            preparedStatement.setInt(2, department.getId());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 1) {
                result = true;
                logger.log(Level.INFO, "The department (id=" + department.getId() + ")" +
                        " was successfully updated in the 'departments' table.");
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
    public boolean delete(Integer id) {
        boolean result = false;
        Connection connection = ConnectionPool.takeConnection();
        try {
            Statement statement = connection.createStatement();
            int rowsDeleted = statement.executeUpdate("delete from postal_service.departments " +
                    "where id=" + id);
            if (rowsDeleted == 1) {
                result = true;
                logger.log(Level.INFO, "The department (id=" + id + ") was successfully removed from the 'departments' table.");
            }
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return result;
    }

    public Optional<Department> getDepartmentByDescription(String description) {
        Optional<Department> optionalDepartment = Optional.empty();
        Connection connection = ConnectionPool.takeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from postal_service.departments where description=?"
            );
            preparedStatement.setString(1, description);
            ResultSet resultSet = preparedStatement.executeQuery();
            Department department = null;
            if (resultSet.next()) {
                department = createDepartmentFromResultSet(resultSet);
            }
            optionalDepartment = Optional.ofNullable(department);
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL query execution error", e);
        } finally {
            ConnectionPool.retrieveConnection(connection);
        }
        return optionalDepartment;
    }
}
