package com.parkinglot.dao;

import com.parkinglot.models.ParkingFloor;
import com.parkinglot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingFloorDAO {

    public List<ParkingFloor> getAllFloors() {
        List<ParkingFloor> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_floors ORDER BY floor_number";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get floors error: " + e.getMessage());
        }
        return list;
    }

    public boolean addFloor(String name, int floorNumber) {
        String sql = "INSERT INTO parking_floors (name, floor_number) VALUES (?, ?)";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, floorNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add floor error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFloor(int floorId) {
        String sql = "DELETE FROM parking_floors WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, floorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete floor error: " + e.getMessage());
            return false;
        }
    }

    private ParkingFloor mapRow(ResultSet rs) throws SQLException {
        return new ParkingFloor(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("floor_number")
        );
    }
}
