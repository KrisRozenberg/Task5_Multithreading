package org.rozenberg.task5.factory;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.task5.entity.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CarsFactory {
    private static final Logger logger = LogManager.getLogger();

    public static Car createCar(double weight, double area) {
        return new Car(weight, area);
    }

    public static List<Car> createAllCars(List<Double> weight, List<Double> area) {
        List<Car> cars = new ArrayList<>();
        ListIterator<Double> weightIterator = weight.listIterator();
        ListIterator<Double> areaIterator = area.listIterator();
        while (weightIterator.hasNext() && areaIterator.hasNext()) {
            cars.add(createCar(weightIterator.next(), areaIterator.next()));
        }
        logger.log(Level.INFO, "Cars configuration is complete");
        return cars;
    }
}
