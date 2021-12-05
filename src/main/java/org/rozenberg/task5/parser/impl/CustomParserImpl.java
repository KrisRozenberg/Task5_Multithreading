package org.rozenberg.task5.parser.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.task5.parser.CustomParser;

import java.util.List;

public class CustomParserImpl implements CustomParser {
    private static final Logger logger = LogManager.getLogger();
    private static final String SPACE_DELIMITER = "\\s+";
    private static CustomParserImpl instance;

    private CustomParserImpl() {}

    public static CustomParserImpl getInstance() {
        if (instance == null) {
            instance = new CustomParserImpl();
        }
        return instance;
    }

    @Override
    public void parse(List<String> carsInfo, List<Double> carsWeight, List<Double> carsArea) {
        carsInfo.forEach(str -> {
            String[] data = str.trim().split(SPACE_DELIMITER);
            carsWeight.add(Double.parseDouble(data[0]));
            carsArea.add(Double.parseDouble(data[1]));
        });
        logger.log(Level.INFO, "Parsing cars data is complete");
    }
}
