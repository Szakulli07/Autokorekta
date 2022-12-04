package com.example.imagepro;

import com.example.imagepro.cars.BlankCar;
import com.example.imagepro.cars.Car;
import com.example.imagepro.cars.newTechnology.NewBiofuelCar;
import com.example.imagepro.cars.newTechnology.NewElectricCar;
import com.example.imagepro.cars.newTechnology.NewHybridCar;
import com.example.imagepro.cars.newTechnology.NewSolarCar;
import com.example.imagepro.cars.oldTechnology.BiofuelCar;
import com.example.imagepro.cars.oldTechnology.ElectricCar;
import com.example.imagepro.cars.oldTechnology.HybridCar;
import com.example.imagepro.cars.oldTechnology.SolarCar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class CarDetector {

    private List<Car> cars = new ArrayList<>();

    private List<Tile> wrongTiles = new ArrayList<>();

    public List<Car> getCars() {
        return cars;
    }

    public List<Tile> getWrongTiles() { return wrongTiles; }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void detectCars(List<Tile> tiles){

        Queue<Tile> startTiles = this.getStartingPoints(tiles);

        Map<Tile, Boolean> seenPoints = new HashMap<>();
        for (Tile tile: tiles) {
            seenPoints.put(tile, Boolean.FALSE);
        }

        List<Car> cars = this.carBFS(startTiles, seenPoints);

        this.setCars(cars);
    }

    private Queue<Tile> getStartingPoints(List<Tile> tiles){
        Queue<Tile> startingPoints = new LinkedList<>();
        for(Tile tile: tiles){
            if(tile.getCarPart().getLabel() == Label.ON_BOARD_COMPUTER){
                startingPoints.add(tile);
            }
        }

        return startingPoints;
    }

    private List<Car> carBFS(Queue<Tile> startPoints, Map<Tile, Boolean> seenPoints){
        List<Car> cars = new ArrayList<>();

        while (!startPoints.isEmpty()){
            Tile startPoint = startPoints.poll();

            if(seenPoints.get(startPoint)) {
                continue;
            }

            Car car;

            switch (startPoint.getLabel()){
                case BIOFUEL:
                    if(!startPoint.isNewTechnology()){
                        car = new BiofuelCar(startPoint);
                    }else{
                        car = new NewBiofuelCar(startPoint);
                    }
                    break;
                case HYBRID:
                    if(!startPoint.isNewTechnology()){
                        car = new HybridCar(startPoint);
                    }else{
                        car = new NewHybridCar( startPoint);
                    }
                    break;
                case ELECTRIC:
                    if(!startPoint.isNewTechnology()){
                        car = new ElectricCar(startPoint);
                    }else{
                        car = new NewElectricCar(startPoint);
                    }
                    break;
                case SOLAR:
                    if(!startPoint.isNewTechnology()){
                        car = new SolarCar(startPoint);
                    }else{
                        car = new NewSolarCar(startPoint);
                    }
                    break;
                default:
                    car = new BlankCar(startPoint);
                    break;
            }

            Queue<Tile> carQueue = new LinkedList<>();

            carQueue.add(startPoint);

            while(!carQueue.isEmpty()){
                Tile tile = carQueue.poll();
                seenPoints.put(tile, Boolean.TRUE);

                for (Tile neighbourTile: tile.getNeighbours()) {

                    if(!seenPoints.get(neighbourTile)
                            && neighbourTile.getRotation() == tile.getRotation()){
                        seenPoints.replace(neighbourTile, Boolean.TRUE);
                        carQueue.add(neighbourTile);
                        car.addTile(neighbourTile);
                    }
                }
            }

            cars.add(car);
        }

        for(Car car: cars){
            car.isValid();
        }

        this.wrongTiles = seenPoints.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == Boolean.FALSE)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        return cars;
    }
}
