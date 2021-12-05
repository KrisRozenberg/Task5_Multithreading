package org.rozenberg.task5.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.task5.exception.CustomException;
import org.rozenberg.task5.util.IdGenerator;

public class Car implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private final long carId;
    private final double weight;
    private final double area;
    private CarState state;

    public Car(double weight, double area) {
        carId = IdGenerator.generateId();
        state = CarState.INITIAL;
        this.weight = weight;
        this.area = area;
    }

    public long getCarId() {
        return carId;
    }

    public CarState getState() {
        return state;
    }

    public double getWeight() {
        return weight;
    }

    public double getArea() {
        return area;
    }

    public void setState(CarState state) {
        this.state = state;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Running {} car-thread", carId);
        FerryBoat boat = FerryBoat.getInstance();
        try {
            boat.takeCarOnBoard(this);
        }
        catch (CustomException e) {
            logger.log(Level.ERROR, "Error while taking car on board: {}", e.getMessage());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Car newCar = (Car) obj;
        return this.carId == newCar.carId && this.state == newCar.state &&
                Double.compare(this.weight, newCar.weight) == 0 && Double.compare(this.area, newCar.area) == 0;
    }

    @Override
    public int hashCode() {
        return (int)((carId ^ (carId >>> 32)) + weight + area * 31 + state.name().hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(carId).append(" car: ").append(weight).append(" weight, ").append(area).append(" area, ")
                .append(state.name()).append(" state");
        return sb.toString();
    }
}
