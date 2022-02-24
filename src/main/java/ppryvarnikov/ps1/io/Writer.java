package ppryvarnikov.ps1.io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Writer {
    private final Logger logger = Logger.getLogger(Writer.class.getName());

    public void writeMesasgeToFile(String command) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.OUTPUT_FILE, true))) {
            writer.write(LocalDateTime.now().
                    format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + command + "\n");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to file " + Constants.OUTPUT_FILE, e);
        }
    }

}
