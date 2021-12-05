package org.rozenberg.task5.reader;

import org.rozenberg.task5.exception.CustomException;

import java.util.List;

public interface CustomReader {
    List<String> readCarsInfo(String resourcePath) throws CustomException;
}
