package org.rozenberg.task5.main;

import org.rozenberg.task5.entity.Car;
import org.rozenberg.task5.exception.CustomException;
import org.rozenberg.task5.factory.CarsFactory;
import org.rozenberg.task5.parser.impl.CustomParserImpl;
import org.rozenberg.task5.reader.impl.CustomReaderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws CustomException {
        CustomReaderImpl reader = CustomReaderImpl.getInstance();
        List<String> data = reader.readCarsInfo("file/carsInfo.txt");
        CustomParserImpl parser = CustomParserImpl.getInstance();
        List<Double> weight = new ArrayList<>();
        List<Double> area = new ArrayList<>();
        parser.parse(data, weight, area);
        List<Car> cars = CarsFactory.createAllCars(weight, area);
        ExecutorService executorService = Executors.newFixedThreadPool(cars.size());
        cars.forEach(executorService::submit);
        executorService.shutdown();
    }
}