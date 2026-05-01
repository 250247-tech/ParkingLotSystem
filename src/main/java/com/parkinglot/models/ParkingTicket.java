package com.parkinglot.models;

import com.parkinglot.enums.ParkingTicketStatus;
import java.time.LocalDateTime;

public class ParkingTicket {
    private int id;
    private String ticketNumber;
    private int vehicleId;
    private int spotId;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;
    private double paidAmount;
    private ParkingTicketStatus status;

    // Extra display fields (joined from DB)
    private String licenseNumber;
    private String spotNumber;
    private String floorName;
    private String vehicleType;

    public ParkingTicket() {}

    public ParkingTicket(int id, String ticketNumber, int vehicleId, int spotId,
                         LocalDateTime issuedAt, LocalDateTime paidAt,
                         double paidAmount, ParkingTicketStatus status) {
        this.id = id;
        this.ticketNumber = ticketNumber;
        this.vehicleId = vehicleId;
        this.spotId = spotId;
        this.issuedAt = issuedAt;
        this.paidAt = paidAt;
        this.paidAmount = paidAmount;
        this.status = status;
    }

    public int getId()                              { return id; }
    public void setId(int id)                       { this.id = id; }
    public String getTicketNumber()                 { return ticketNumber; }
    public void setTicketNumber(String t)           { this.ticketNumber = t; }
    public int getVehicleId()                       { return vehicleId; }
    public void setVehicleId(int v)                 { this.vehicleId = v; }
    public int getSpotId()                          { return spotId; }
    public void setSpotId(int s)                    { this.spotId = s; }
    public LocalDateTime getIssuedAt()              { return issuedAt; }
    public void setIssuedAt(LocalDateTime t)        { this.issuedAt = t; }
    public LocalDateTime getPaidAt()                { return paidAt; }
    public void setPaidAt(LocalDateTime t)          { this.paidAt = t; }
    public double getPaidAmount()                   { return paidAmount; }
    public void setPaidAmount(double a)             { this.paidAmount = a; }
    public ParkingTicketStatus getStatus()          { return status; }
    public void setStatus(ParkingTicketStatus s)    { this.status = s; }

    public String getLicenseNumber()                { return licenseNumber; }
    public void setLicenseNumber(String l)          { this.licenseNumber = l; }
    public String getSpotNumber()                   { return spotNumber; }
    public void setSpotNumber(String s)             { this.spotNumber = s; }
    public String getFloorName()                    { return floorName; }
    public void setFloorName(String f)              { this.floorName = f; }
    public String getVehicleType()                  { return vehicleType; }
    public void setVehicleType(String v)            { this.vehicleType = v; }

    @Override
    public String toString() {
        return ticketNumber + " | " + licenseNumber + " | " + status;
    }
}
