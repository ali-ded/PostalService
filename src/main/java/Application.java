import ppryvarnikov.ps1.io.Constants;
import ppryvarnikov.ps1.service.PostalService;

import java.io.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Application {
    static {
        File logDir = new File(Constants.LOG_DIR);
        if (!logDir.exists() && !logDir.mkdirs()) {
            System.err.println("Unable to create log folder.");
        }
        try {
            LogManager.getLogManager().readConfiguration(
                    Application.class.getResourceAsStream(Constants.LOG_PROPERTIES));
        } catch (IOException | NullPointerException e) {
            System.err.println("Logging setup error.");
            e.printStackTrace();
        }
    }

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        logger.log(Level.INFO, "Application launched successfully.");

        PostalService postalService = new PostalService();
        postalService.start();

    }
}
