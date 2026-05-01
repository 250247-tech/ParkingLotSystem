package com.parkinglot.dao;

import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.models.ParkingSpot;
import com.parkinglot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotDAO {

    public List<ParkingSpot> getAllSpots() {
        List<ParkingSpot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots ORDER BY floor_id, spot_number";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get spots error: " + e.getMessage());
        }
        return list;
    }

    public List<ParkingSpot> getSpotsByFloor(int floorId) {
        List<ParkingSpot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE floor_id = ? ORDER BY spot_number";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, floorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get spots by floor error: " + e.getMessage());
        }
        return list;
    }

    public List<ParkingSpot> getFreeSpots() {
        List<ParkingSpot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_spots WHERE is_free = TRUE ORDER BY floor_id, spot_number";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get free spots error: " + e.getMessage());
        }
        return list;
    }

    public boolean addSpot(String spotNumber, int floorId, ParkingSpotType type) {
        String sql = "INSERT INTO parking_spots (spot_number, floor_id, spot_type, is_free) VALUES (?,?,?,TRUE)";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, spotNumber);
            ps.setInt(2, floorId);
            ps.setString(3, type.name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add spot error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSpot(int spotId) {
        String sql = "DELETE FROM parking_spots WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, spotId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete spot error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateSpotAvailability(int spotId, boolean isFree) {
        String sql = "UPDATE parking_spots SET is_free = ? WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, isFree);
            ps.setInt(2, spotId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update spot error: " + e.getMessage());
            return false;
        }
    }

    /** Count free spots grouped by type for display board */
    public int countFreeSpotsByType(ParkingSpotType type) {
        String sql = "SELECT COUNT(*) FROM parking_spots WHERE spot_type = ? AND is_free = TRUE";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Count spots error: " + e.getMessage());
        }
        return 0;
    }

    /** Total spots for capacity check */
    public int countTotalSpots() {
        String sql = "SELECT COUNT(*) FROM parking_spots";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Count total error: " + e.getMessage());
        }
        return 0;
    }

    public int countFreeSpots() {
        String sql = "SELECT COUNT(*) FROM parking_spots WHERE is_free = TRUE";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Count free error: " + e.getMessage());
        }
        return 0;
    }

    private ParkingSpot mapRow(ResultSet rs) throws SQLException {
        return new ParkingSpot(
                rs.getInt("id"),
                rs.getString("spot_number"),
                rs.getInt("floor_id"),
                ParkingSpotType.valueOf(rs.getString("spot_type")),
                rs.getBoolean("is_free")
        );
    }
}
