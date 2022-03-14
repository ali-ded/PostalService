package ppryvarnikov.ps1.dao;

import ppryvarnikov.ps1.io.Constants;
import ppryvarnikov.ps1.io.Reader;

import java.security.InvalidParameterException;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPool implements Constants {
    private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());

    private static BlockingQueue<Connection> connectionPool;
    private static final int CAPACITY = 20;

    private ConnectionPool() {
    }

    private static void createConnectionPool() {
        try {
            logger.info("Registering JDBC driver...");
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "JDBC driver registration error.", e);
            return;
        }

        Reader reader = new Reader();
        List<String> dbSettings = reader.getStringList(DIR, DB_CON_SET);

        if (dbSettings.size() != 3) {
            Exception exception = new InvalidParameterException();
            logger.log(Level.SEVERE,
                    "Insufficient data to connect to the database. Check file " + DB_CON_SET,
                    exception);
            return;
        }

        try {
            logger.info("Creating database connection pool...");
            Connection connection = createConnection(dbSettings);
            int connectionLimit = 1;
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT rolconnlimit " +
                    "FROM pg_roles WHERE rolname=?");
            preparedStatement.setString(1, dbSettings.get(1));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connectionLimit = resultSet.getInt(1);
            }
            if (connectionLimit > CAPACITY || connectionLimit == -1) {
                connectionLimit = CAPACITY;
            }
            resultSet.close();
            preparedStatement.close();

            connectionPool = new ArrayBlockingQueue<>(connectionLimit);
            connectionPool.put(connection);
            for (int i = 1; i < connectionLimit; i++) {
                connection = createConnection(dbSettings);
                connectionPool.put(connection);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING,
                    "The allowed limit of connections to the database " +
                            "for the user " + dbSettings.get(1) + " was exceeded.",
                    e);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error adding connection to pool", e);
        }
    }

    private static Connection createConnection(List<String> dbSettings) throws SQLException {
        return DriverManager.getConnection(JDBC_DATABASE + dbSettings.get(0),
                dbSettings.get(1),
                dbSettings.get(2));
    }

    public static Connection takeConnection() {
        if (connectionPool == null) {
            createConnectionPool();
        }
        Optional<Connection> optionalConnection = Optional.empty();
        try {
            optionalConnection = Optional.of(connectionPool.take());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Unable to connect to database.", e);
        }
        return optionalConnection.orElseThrow();
    }

    public static void retrieveConnection(Connection connection) {
        if (connection != null) {
            try {
                connectionPool.put(connection);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Unable to return connection to connection pool.", e);
            }
        }
    }

    public static void close() {
        for (Connection connection : connectionPool) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error when trying to close database connection.", e);
            }
        }
    }
}
