package com.parkinglot.dao;

import com.parkinglot.enums.AccountRole;
import com.parkinglot.enums.AccountStatus;
import com.parkinglot.models.Account;
import com.parkinglot.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    /** Returns account if credentials match, null otherwise */
    public Account login(String username, String password) {
        String sql = "SELECT * FROM accounts WHERE username = ? AND password = ? AND status = 'ACTIVE'";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }

    /** Returns all attendant accounts */
    public List<Account> getAllAttendants() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE role = 'ATTENDANT'";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get attendants error: " + e.getMessage());
        }
        return list;
    }

    /** Returns all accounts */
    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (Connection c = DatabaseUtil.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Get accounts error: " + e.getMessage());
        }
        return list;
    }

    /** Adds a new attendant account */
    public boolean addAttendant(String username, String password, String email) {
        String sql = "INSERT INTO accounts (username, password, email, role, status) VALUES (?,?,?,'ATTENDANT','ACTIVE')";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add attendant error: " + e.getMessage());
            return false;
        }
    }

    /** Removes an account by ID */
    public boolean removeAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Remove account error: " + e.getMessage());
            return false;
        }
    }

    /** Checks if username already exists */
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM accounts WHERE username = ?";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                AccountRole.valueOf(rs.getString("role")),
                AccountStatus.valueOf(rs.getString("status"))
        );
    }
}
