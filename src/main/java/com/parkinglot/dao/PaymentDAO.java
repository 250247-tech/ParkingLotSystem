package com.parkinglot.dao;

import com.parkinglot.enums.PaymentStatus;
import com.parkinglot.enums.PaymentType;
import com.parkinglot.models.Payment;
import com.parkinglot.util.DatabaseUtil;

import java.sql.*;

public class PaymentDAO {

    public boolean savePayment(Payment payment) {
        String sql = "INSERT INTO payments (ticket_id, amount, payment_type, status, card_name, cash_tendered) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, payment.getTicketId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getPaymentType().name());
            ps.setString(4, payment.getStatus().name());
            ps.setString(5, payment.getCardName());
            ps.setDouble(6, payment.getCashTendered());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Save payment error: " + e.getMessage());
            return false;
        }
    }
}
