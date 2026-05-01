package com.parkinglot.ui;

import com.parkinglot.models.Account;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;
    private static Account loggedInAccount;

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("🅿 Parking Lot System");
        primaryStage.setResizable(true);
    }

    public static void showLogin() {
        LoginScene scene = new LoginScene();
        primaryStage.setScene(scene.getScene());
        primaryStage.setTitle("🅿 Parking Lot System — Login");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void showAdminDashboard() {
        AdminDashboardScene scene = new AdminDashboardScene();
        primaryStage.setScene(scene.getScene());
        primaryStage.setTitle("🅿 Admin Dashboard");
        primaryStage.centerOnScreen();
    }

    public static void showAttendantDashboard() {
        AttendantDashboardScene scene = new AttendantDashboardScene();
        primaryStage.setScene(scene.getScene());
        primaryStage.setTitle("🅿 Attendant Dashboard");
        primaryStage.centerOnScreen();
    }

    public static void showPaymentScene(String ticketNumber) {
        PaymentScene scene = new PaymentScene(ticketNumber);
        primaryStage.setScene(scene.getScene());
        primaryStage.setTitle("🅿 Process Payment");
        primaryStage.centerOnScreen();
    }

    public static Stage getStage()                      { return primaryStage; }
    public static Account getLoggedInAccount()          { return loggedInAccount; }
    public static void setLoggedInAccount(Account acc)  { loggedInAccount = acc; }
}
