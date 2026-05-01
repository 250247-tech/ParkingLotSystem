package com.parkinglot.ui;

import com.parkinglot.dao.AccountDAO;
import com.parkinglot.enums.AccountRole;
import com.parkinglot.models.Account;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class LoginScene {

    private final Scene scene;

    public LoginScene() {
        // ── Root ──────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT_BG);

        // ── Login Card ────────────────────────────────────────
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setStyle(Styles.CARD_BG);

        // Logo / Title
        Text logo = new Text("🅿");
        logo.setStyle("-fx-font-size: 48px;");

        Text title = new Text("Parking Lot System");
        title.setStyle(Styles.TITLE);

        Text subtitle = new Text("Staff Login Portal");
        subtitle.setStyle(Styles.SUBTITLE);

        // Separator
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #2E3D52;");

        // Username
        Label userLabel = new Label("Username");
        userLabel.setStyle(Styles.LABEL);
        userLabel.setMaxWidth(Double.MAX_VALUE);
        TextField userField = new TextField();
        userField.setPromptText("Enter username");
        userField.setStyle(Styles.TEXT_FIELD);
        userField.setMaxWidth(Double.MAX_VALUE);

        // Password
        Label passLabel = new Label("Password");
        passLabel.setStyle(Styles.LABEL);
        passLabel.setMaxWidth(Double.MAX_VALUE);
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");
        passField.setStyle(Styles.TEXT_FIELD);
        passField.setMaxWidth(Double.MAX_VALUE);

        // Status label
        Label statusLabel = new Label();
        statusLabel.setStyle(Styles.MSG_ERROR);
        statusLabel.setWrapText(true);

        // Login Button
        Button loginBtn = new Button("Log In");
        loginBtn.setStyle(Styles.BTN_PRIMARY);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(Styles.BTN_PRIMARY_HOVER));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(Styles.BTN_PRIMARY));

        // Hint
        Text hint = new Text("Default: admin / admin123  |  attendant1 / attend123");
        hint.setStyle("-fx-font-size: 11px; -fx-fill: #3D5166;");

        // ── Login Action ──────────────────────────────────────
        Runnable doLogin = () -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();
            if (user.isEmpty() || pass.isEmpty()) {
                statusLabel.setText("Please enter both username and password.");
                return;
            }
            AccountDAO dao = new AccountDAO();
            Account account = dao.login(user, pass);
            if (account == null) {
                statusLabel.setText("Incorrect username or password.");
                passField.clear();
            } else {
                SceneManager.setLoggedInAccount(account);
                if (account.getRole() == AccountRole.ADMIN) {
                    SceneManager.showAdminDashboard();
                } else {
                    SceneManager.showAttendantDashboard();
                }
            }
        };

        loginBtn.setOnAction(e -> doLogin.run());
        passField.setOnAction(e -> doLogin.run());

        card.getChildren().addAll(
                logo, title, subtitle, sep,
                userLabel, userField,
                passLabel, passField,
                statusLabel, loginBtn, hint
        );

        BorderPane.setAlignment(card, Pos.CENTER);
        root.setCenter(card);

        scene = new Scene(root, 500, 580);
    }

    public Scene getScene() { return scene; }
}
