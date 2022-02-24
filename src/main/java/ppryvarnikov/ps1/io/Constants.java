package ppryvarnikov.ps1.io;

public interface Constants {
    String SLASH = getSlash();
    String DIR = "src" + SLASH + "main" + SLASH + "resources" + SLASH;
    String INPUT_FILE = "commands.txt";
    String OUTPUT_FILE = DIR + "applicationResult.txt";
    String DB_CON_SET = "dbConnectionSettings.txt";
    String TABLES = "tables.sql";
    String LOG_DIR = "logs";
    String LOG_PROPERTIES = "logging.properties";
    String JDBC_DRIVER = "org.postgresql.Driver";
    String JDBC_DATABASE = "jdbc:postgresql:";

    private static String getSlash() {
        String slash;
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            slash = "\\";
        } else {
            slash = "/";
        }
        return slash;
    }
}
