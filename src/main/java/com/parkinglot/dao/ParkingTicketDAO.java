package com.parkinglot.dao;

import com.parkinglot.enums.ParkingTicketStatus;
import com.parkinglot.models.ParkingTicket;
import com.parkinglot.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingTicketDAO {

    /** Creates a new ticket, returns generated ticket number */
    public String createTicket(int vehicleId, int spotId) {
        String ticketNumber = "TKT-" + System.currentTimeMillis();
        String sql = "INSERT INTO parking_tickets (ticket_number, vehicle_id, spot_id, issued_at, status) " +
                "VALUES (?, ?, ?, NOW(), 'ACTIVE')";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ticketNumber);
            ps.setInt(2, vehicleId);
            ps.setInt(3, spotId);
            ps.executeUpdate();
            return ticketNumber;
        } catch (SQLException e) {
            System.out.println("Create ticket error: " + e.getMessage());
            return null;
        }
    }

    /** Finds ticket by ticket number with joined vehicle and spot info */
    public ParkingTicket getByTicketNumber(String ticketNumber) {
        String sql = "SELECT pt.*, v.license_number, v.vehicle_type, " +
                "ps.spot_number, pf.name AS floor_name " +
                "FROM parking_tickets pt " +
                "JOIN vehicles v ON pt.vehicle_id = v.id " +
                "JOIN parking_spots ps ON pt.spot_id = ps.id " +
                "JOIN parking_floors pf ON ps.floor_id = pf.id " +
                "WHERE pt.ticket_number = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ticketNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Get ticket error: " + e.getMessage());
        }
        return null;
    }

    /** Gets all active tickets */
    public List<ParkingTicket> getAllActiveTickets() {
        return getTicketsByStatus("ACTIVE");
    }

    /** Gets all tickets */
    public List<ParkingTicket> getAllTickets() {
        List<ParkingTicket> list = new ArrayList<>();
        String sql = "SELECT pt.*, v.license_number, v.vehicle_type, " +
                "ps.spot_number, pf.name AS floor_name " +
                "FROM parking_tickets pt " +
                "JOIN vehicles v ON pt.vehicle_id = v.id " +
                "JOIN parking_spots ps ON pt.spot_id = ps.id " +
                "JOIN parking_floors pf ON ps.floor_id = pf.id " +
                "ORDER BY pt.issued_at DESC";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get all tickets error: " + e.getMessage());
        }
        return list;
    }

    public List<ParkingTicket> getTicketsByStatus(String status) {
        List<ParkingTicket> list = new ArrayList<>();
        String sql = "SELECT pt.*, v.license_number, v.vehicle_type, " +
                "ps.spot_number, pf.name AS floor_name " +
                "FROM parking_tickets pt " +
                "JOIN vehicles v ON pt.vehicle_id = v.id " +
                "JOIN parking_spots ps ON pt.spot_id = ps.id " +
                "JOIN parking_floors pf ON ps.floor_id = pf.id " +
                "WHERE pt.status = ? ORDER BY pt.issued_at DESC";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get tickets by status error: " + e.getMessage());
        }
        return list;
    }

    /** Marks ticket as paid */
    public boolean markAsPaid(int ticketId, double amount) {
        String sql = "UPDATE parking_tickets SET status='PAID', paid_at=NOW(), paid_amount=? WHERE id=?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, ticketId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Mark paid error: " + e.getMessage());
            return false;
        }
    }

    private ParkingTicket mapRow(ResultSet rs) throws SQLException {
        ParkingTicket t = new ParkingTicket();
        t.setId(rs.getInt("id"));
        t.setTicketNumber(rs.getString("ticket_number"));
        t.setVehicleId(rs.getInt("vehicle_id"));
        t.setSpotId(rs.getInt("spot_id"));
        Timestamp issuedTs = rs.getTimestamp("issued_at");
        if (issuedTs != null) t.setIssuedAt(issuedTs.toLocalDateTime());
        Timestamp paidTs = rs.getTimestamp("paid_at");
        if (paidTs != null) t.setPaidAt(paidTs.toLocalDateTime());
        t.setPaidAmount(rs.getDouble("paid_amount"));
        t.setStatus(ParkingTicketStatus.valueOf(rs.getString("status")));
        t.setLicenseNumber(rs.getString("license_number"));
        t.setVehicleType(rs.getString("vehicle_type"));
        t.setSpotNumber(rs.getString("spot_number"));
        t.setFloorName(rs.getString("floor_name"));
        return t;
    }
}
