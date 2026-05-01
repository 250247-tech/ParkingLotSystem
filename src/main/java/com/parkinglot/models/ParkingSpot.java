package com.parkinglot.models;

import com.parkinglot.enums.ParkingSpotType;

public class ParkingSpot {
    private int id;
    private String spotNumber;
    private int floorId;
    private ParkingSpotType type;
    private boolean free;

    public ParkingSpot() {}

    public ParkingSpot(int id, String spotNumber, int floorId,
                       ParkingSpotType type, boolean free) {
        this.id = id;
        this.spotNumber = spotNumber;
        this.floorId = floorId;
        this.type = type;
        this.free = free;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public String getSpotNumber()               { return spotNumber; }
    public void setSpotNumber(String n)         { this.spotNumber = n; }
    public int getFloorId()                     { return floorId; }
    public void setFloorId(int fid)             { this.floorId = fid; }
    public ParkingSpotType getType()            { return type; }
    public void setType(ParkingSpotType t)      { this.type = t; }
    public boolean isFree()                     { return free; }
    public void setFree(boolean free)           { this.free = free; }

    public boolean getIsFree()                  { return free; }

    @Override
    public String toString() {
        return spotNumber + " [" + type + "] " + (free ? "FREE" : "OCCUPIED");
    }
}
