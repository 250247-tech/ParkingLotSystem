package com.parkinglot.models;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    private int id;
    private String name;
    private int floorNumber;
    private List<ParkingSpot> spots;

    public ParkingFloor() {
        this.spots = new ArrayList<>();
    }

    public ParkingFloor(int id, String name, int floorNumber) {
        this.id = id;
        this.name = name;
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
    }

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }
    public int getFloorNumber()             { return floorNumber; }
    public void setFloorNumber(int n)       { this.floorNumber = n; }
    public List<ParkingSpot> getSpots()     { return spots; }
    public void setSpots(List<ParkingSpot> spots) { this.spots = spots; }

    public void addParkingSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public void updateDisplayBoard() {
        long free = spots.stream().filter(ParkingSpot::isFree).count();
        System.out.println(name + ": " + free + " free spots");
    }

    @Override
    public String toString() {
        return name + " (Floor " + floorNumber + ")";
    }
}
