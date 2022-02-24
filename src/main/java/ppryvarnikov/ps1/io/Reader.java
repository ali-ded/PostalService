package ppryvarnikov.ps1.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Reader {
    private final Logger logger = Logger.getLogger(Reader.class.getName());

    public String getString(String path) {
        StringBuilder stringLines = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                stringLines.append(tmp).append("\n");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                    "There was a problem while trying to read from a file " + path,
                    e);
        }
        return stringLines.toString();
    }

    public List<String> getStringList(String dirName, String fileName) {
        Path path = FileSystems.getDefault().getPath(dirName, fileName);
        List<String> stringList = new ArrayList<>();
        try {
            stringList = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                    "There was a problem while trying to read from a file " + path,
                    e);
        }

        stringList = stringList.stream()
                .filter(line -> !line.equals(""))
                .filter(line -> !line.startsWith("#"))
                .collect(Collectors.toList());

        return stringList;
    }
}
