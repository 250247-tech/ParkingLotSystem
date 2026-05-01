package com.parkinglot.models;

public class ParkingRate {
    private int id;
    private int hourNumber;
    private double rate;

    public ParkingRate() {}

    public ParkingRate(int id, int hourNumber, double rate) {
        this.id = id;
        this.hourNumber = hourNumber;
        this.rate = rate;
    }

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }
    public int getHourNumber()      { return hourNumber; }
    public void setHourNumber(int h){ this.hourNumber = h; }
    public double getRate()         { return rate; }
    public void setRate(double r)   { this.rate = r; }

    @Override
    public String toString() {
        return "Hour " + hourNumber + ": $" + rate;
    }
}
