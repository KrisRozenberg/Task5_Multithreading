package org.rozenberg.task5.reader.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.task5.exception.CustomException;
import org.rozenberg.task5.reader.CustomReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

public class CustomReaderImpl implements CustomReader{
    private static final Logger logger = LogManager.getLogger();
    private static CustomReaderImpl instance;

    private CustomReaderImpl() {}

    public static CustomReaderImpl getInstance() {
        if (instance == null) {
            instance = new CustomReaderImpl();
        }
        return instance;
    }

    @Override
    public List<String> readCarsInfo(String resourcePath) throws CustomException {
        if (resourcePath == null) {
            logger.log(Level.ERROR, "Provided filepath is null");
            throw new CustomException("Provided filepath is null");
        }
        List<String> carsInfo;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null || stream.available() == 0) {
                logger.log(Level.ERROR, "Provided filepath is corrupt or file is empty");
                throw new CustomException("Provided filepath is corrupt or file is empty");
            }
            try (InputStreamReader in = new InputStreamReader(stream);
                 BufferedReader bufferedReader = new BufferedReader(in);
                 Stream<String> linesStream = bufferedReader.lines()) {
                carsInfo = linesStream.toList();
            }
        }
        catch (IOException e) {
            logger.log(Level.ERROR, "Error while reading file");
            throw new CustomException("Error while reading file", e);
        }
        logger.log(Level.INFO, "{} file reading is complete", resourcePath);
        return carsInfo;
    }
}