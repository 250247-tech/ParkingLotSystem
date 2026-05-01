package com.parkinglot.models;

import com.parkinglot.enums.VehicleType;

public class Vehicle {
    private int id;
    private String licenseNumber;
    private VehicleType vehicleType;

    public Vehicle() {}

    public Vehicle(int id, String licenseNumber, VehicleType vehicleType) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.vehicleType = vehicleType;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public String getLicenseNumber()            { return licenseNumber; }
    public void setLicenseNumber(String n)      { this.licenseNumber = n; }
    public VehicleType getVehicleType()         { return vehicleType; }
    public void setVehicleType(VehicleType t)   { this.vehicleType = t; }

    public void assignTicket(ParkingTicket ticket) {
        System.out.println("Ticket " + ticket.getTicketNumber()
                + " assigned to vehicle " + licenseNumber);
    }

    @Override
    public String toString() {
        return licenseNumber + " (" + vehicleType + ")";
    }
}

// ── Subclasses (Polymorphism) ────────────────────────────────
class Car extends Vehicle {
    public Car(int id, String licenseNumber) {
        super(id, licenseNumber, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    public Truck(int id, String licenseNumber) {
        super(id, licenseNumber, VehicleType.TRUCK);
    }
}

class ElectricVehicle extends Vehicle {
    public ElectricVehicle(int id, String licenseNumber) {
        super(id, licenseNumber, VehicleType.ELECTRIC);
    }
}

class Van extends Vehicle {
    public Van(int id, String licenseNumber) {
        super(id, licenseNumber, VehicleType.VAN);
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(int id, String licenseNumber) {
        super(id, licenseNumber, VehicleType.MOTORCYCLE);
    }
}
