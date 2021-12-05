package org.rozenberg.task5.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rozenberg.task5.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class FerryBoat {
    private static final Logger logger = LogManager.getLogger();
    private static final double WEIGHT_CAPACITY = 3000;
    private static final double AREA_CAPACITY = 900;
    private static final ReentrantLock instanceLocker = new ReentrantLock();
    private static final AtomicBoolean instanceIsCreated = new AtomicBoolean();
    private static FerryBoat instance;
    private final ReentrantLock carLocker = new ReentrantLock(true);
    private double takenWeight;
    private double takenArea;
    private final List<Car> carsOnBoard = new ArrayList<>();

    private FerryBoat() {
    }

    public static FerryBoat getInstance() {
        if (!instanceIsCreated.get()) {
            instanceLocker.lock();
            try {
                if (instance == null) {
                    instance = new FerryBoat();
                    instanceIsCreated.set(true);
                }
            } finally {
                instanceLocker.unlock();
            }
        }
        return instance;
    }

    public double getTakenWeight() {
        return takenWeight;
    }

    public double getTakenArea() {
        return takenArea;
    }

    public void takeCarOnBoard(Car car) throws CustomException {
        try {
            if (carLocker.isLocked()) {
                car.setState(CarState.IN_QUEUE);
                logger.log(Level.INFO, " car {} in queue to the boat", car.getCarId());
            }
            carLocker.lock();
            if (!canTakeOnBoard(car)) {
                logger.log(Level.INFO, "The ferry boat is full and crossing the river. {} car is waiting for the boat", car.getCarId());
                shipCars();
            }
            TimeUnit.SECONDS.sleep(1);
            takenWeight += car.getWeight();
            takenArea += car.getArea();
            car.setState(CarState.ON_BOARD);
            carsOnBoard.add(car);
            logger.log(Level.INFO, "{} car is on boat", car.getCarId());
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Car-thread was interrupted while waiting the boat");
            throw new CustomException("Car-thread was interrupted while waiting the boat: ", e);
        } finally {
            carLocker.unlock();
        }
    }

    private boolean canTakeOnBoard(Car car) {
        return (takenWeight + car.getWeight()) <= WEIGHT_CAPACITY && (takenArea + car.getArea()) <= AREA_CAPACITY;
    }

    private void shipCars() {
        try {
            TimeUnit.SECONDS.sleep(5);
            takenWeight = 0;
            takenArea = 0;
            carsOnBoard.forEach(shippedCar -> shippedCar.setState(CarState.TRANSPORTED));
            carsOnBoard.clear();
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Car-thread was interrupted while waiting the boat");
        }

    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return (int) (takenWeight + 31 * takenArea + carsOnBoard.size());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ferry boat status: ").append(takenWeight).append("/").append(WEIGHT_CAPACITY).append(" kg filled; ")
                .append(takenArea).append("/").append(AREA_CAPACITY).append(" square meters filled; ")
                .append(carsOnBoard.size()).append(" cars on board");
        return sb.toString();
    }
}
