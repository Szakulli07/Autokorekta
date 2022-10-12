package com.example.imagepro;

import com.example.imagepro.cars.BiofuelCar;
import com.example.imagepro.cars.BlankCar;
import com.example.imagepro.cars.Car;
import com.example.imagepro.cars.ElectricCar;
import com.example.imagepro.cars.HybridCar;
import com.example.imagepro.cars.SolarCar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class CarDetector {

    private List<Tile> cars = new ArrayList<>();

    public List<Tile> getCars() {
        return cars;
    }

    public void setCars(List<Tile> cars) {
        this.cars = cars;
    }

    public void detectCars(List<Tile> tiles){
        Map<Boolean, List<Tile>> partitions = tiles.stream()
                .collect(Collectors.partitioningBy(tile -> tile.getCarPart().getLabel() == Label.CHASSIS));

        if(partitions.get(true) == null) {
            return;
        }

        Queue<Tile> startTiles = new LinkedList<>(partitions.get(true));

        Map<Tile, Boolean> seenPoints = new HashMap<>();
        for (Tile tile: tiles) {
            seenPoints.put(tile, Boolean.FALSE);
        }

        List<Car> beforeRotationCars = this.carBFS(startTiles, seenPoints);

        this.setCars(tiles);
    }

    private List<Car> carBFS(Queue<Tile> startPoints, Map<Tile, Boolean> seenPoints){
        List<Car> cars = new ArrayList<>();

        while (!startPoints.isEmpty()){
            Tile startPoint = startPoints.poll();
            Car car;

            switch (startPoint.getCarType()){
                case BIOFUEL:
                    car = new BiofuelCar(startPoint);
                    break;
                case HYBRID:
                    car = new HybridCar(startPoint);
                    break;
                case ELECTRIC:
                    car = new ElectricCar(startPoint);
                    break;
                case SOLAR:
                    car = new SolarCar(startPoint);
                    break;
                default:
                    car = new BlankCar(startPoint);
                    break;
            }

            Queue<Tile> carQueue = new LinkedList<>();

            if(startPoint.getCarType() != Label.BLANK){
                carQueue.add(startPoint);
            }

            while(!carQueue.isEmpty()){
                Tile tile = carQueue.poll();
                seenPoints.put(tile, Boolean.TRUE);

                for (Tile neighbourTile: tile.getNeighbours()) {

                    if(!seenPoints.get(neighbourTile)
                            && neighbourTile.getRotation() == tile.getRotation()
                            && neighbourTile.getCarType() == tile.getCarType()){
                        seenPoints.replace(neighbourTile, Boolean.TRUE);
                        carQueue.add(neighbourTile);
                        car.addTile(neighbourTile);
                    }
                }
            }

            if(car.isValid()){
                cars.add(car);
            }
        }

        return cars;
    }
}
