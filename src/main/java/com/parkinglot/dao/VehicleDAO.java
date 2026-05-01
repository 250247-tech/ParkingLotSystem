package com.parkinglot.dao;

import com.parkinglot.enums.VehicleType;
import com.parkinglot.models.Vehicle;
import com.parkinglot.util.DatabaseUtil;

import java.sql.*;

public class VehicleDAO {

    /** Returns existing vehicle or inserts new one, returns vehicle ID */
    public int getOrCreateVehicle(String licenseNumber, VehicleType type) {
        // Try to find existing
        String find = "SELECT id FROM vehicles WHERE license_number = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(find)) {
            ps.setString(1, licenseNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("Find vehicle error: " + e.getMessage());
        }

        // Insert new
        String insert = "INSERT INTO vehicles (license_number, vehicle_type) VALUES (?,?)";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, licenseNumber);
            ps.setString(2, type.name());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.out.println("Create vehicle error: " + e.getMessage());
        }
        return -1;
    }

    public Vehicle getByLicense(String licenseNumber) {
        String sql = "SELECT * FROM vehicles WHERE license_number = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, licenseNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Vehicle(
                        rs.getInt("id"),
                        rs.getString("license_number"),
                        VehicleType.valueOf(rs.getString("vehicle_type"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Get vehicle error: " + e.getMessage());
        }
        return null;
    }
}
