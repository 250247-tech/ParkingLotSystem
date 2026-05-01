package com.parkinglot.ui;

import com.parkinglot.dao.*;
import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.models.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class AttendantDashboardScene {

    private final Scene scene;
    private final ParkingSpotDAO spotDAO = new ParkingSpotDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final ParkingTicketDAO ticketDAO = new ParkingTicketDAO();

    public AttendantDashboardScene() {
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT_BG);
        root.setTop(buildTopBar());

        VBox sidebar = buildSidebar(root);
        root.setLeft(sidebar);
        root.setCenter(buildDisplayBoardPane());

        scene = new Scene(root, 1100, 700);
    }

    // ─────────────────────────────────────────────────────────
    //  Top Bar
    // ─────────────────────────────────────────────────────────
    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 20, 14, 20));
        bar.setStyle("-fx-background-color: #111D2B; -fx-border-color: #1E3048; -fx-border-width: 0 0 1 0;");

        Text logo = new Text("🅿  Parking Lot System");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #FFFFFF; -fx-font-family: 'Courier New';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Account acc = SceneManager.getLoggedInAccount();
        Label userLabel = new Label("👤 " + acc.getUsername() + "  |  ATTENDANT");
        userLabel.setStyle(Styles.LABEL);

        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle(Styles.BTN_LOGOUT);
        logoutBtn.setOnAction(e -> SceneManager.showLogin());

        bar.getChildren().addAll(logo, spacer, userLabel, new Label("   "), logoutBtn);
        return bar;
    }

    // ─────────────────────────────────────────────────────────
    //  Sidebar
    // ─────────────────────────────────────────────────────────
    private VBox buildSidebar(BorderPane root) {
        VBox sidebar = new VBox(4);
        sidebar.setPrefWidth(200);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle(Styles.SIDEBAR);

        Label menuLabel = new Label("MENU");
        menuLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #3D5166; -fx-font-weight: bold; -fx-padding: 0 0 8 8;");

        String[] items = {"📋 Display Board", "🎫 Issue Ticket", "🔍 Scan Ticket", "💳 Active Tickets"};
        Button[] btns = new Button[items.length];

        for (int i = 0; i < items.length; i++) {
            btns[i] = new Button(items[i]);
            btns[i].setMaxWidth(Double.MAX_VALUE);
            btns[i].setStyle(i == 0 ? Styles.NAV_BTN_ACTIVE : Styles.NAV_BTN);
        }

        btns[0].setOnAction(e -> { root.setCenter(buildDisplayBoardPane()); setActive(btns, 0); });
        btns[1].setOnAction(e -> { root.setCenter(buildIssueTicketPane());  setActive(btns, 1); });
        btns[2].setOnAction(e -> { root.setCenter(buildScanTicketPane());   setActive(btns, 2); });
        btns[3].setOnAction(e -> { root.setCenter(buildActiveTicketsPane());setActive(btns, 3); });

        sidebar.getChildren().add(menuLabel);
        sidebar.getChildren().addAll(btns);
        return sidebar;
    }

    private void setActive(Button[] btns, int active) {
        for (int i = 0; i < btns.length; i++)
            btns[i].setStyle(i == active ? Styles.NAV_BTN_ACTIVE : Styles.NAV_BTN);
    }

    // ─────────────────────────────────────────────────────────
    //  Display Board
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildDisplayBoardPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("Parking Display Board");
        heading.setStyle(Styles.TITLE);

        int free  = spotDAO.countFreeSpots();
        int total = spotDAO.countTotalSpots();

        Label capacityLabel = new Label("Capacity: " + free + " / " + total + " spots free");
        capacityLabel.setStyle(Styles.VALUE_TEXT);

        if (free == 0) {
            Label fullMsg = new Label("⚠  PARKING LOT IS FULL — No available spots");
            fullMsg.setStyle("-fx-background-color: #4D1E1E; -fx-text-fill: #E74C3C; " +
                    "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 20; " +
                    "-fx-background-radius: 8;");
            pane.getChildren().addAll(heading, capacityLabel, fullMsg);
        }

        // Per-type display board
        GridPane board = new GridPane();
        board.setHgap(16); board.setVgap(16);
        board.setPadding(new Insets(16));
        board.setStyle(Styles.CARD_BG);

        ParkingSpotType[] types = ParkingSpotType.values();
        for (int i = 0; i < types.length; i++) {
            int freeCount = spotDAO.countFreeSpotsByType(types[i]);
            VBox tile = new VBox(6);
            tile.setAlignment(Pos.CENTER);
            tile.setPrefWidth(160);
            tile.setPrefHeight(90);
            tile.setStyle(freeCount > 0 ? Styles.SPOT_FREE : Styles.SPOT_OCCUPIED);

            Label typeLabel = new Label(types[i].name());
            typeLabel.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 12px; -fx-font-weight: bold;");

            Label countLabel = new Label(freeCount > 0 ? freeCount + " FREE" : "FULL");
            countLabel.setStyle("-fx-text-fill: " + (freeCount > 0 ? "#27AE60" : "#E74C3C") +
                    "; -fx-font-size: 22px; -fx-font-weight: bold;");
            tile.getChildren().addAll(typeLabel, countLabel);
            board.add(tile, i % 3, i / 3);
        }

        Button refreshBtn = new Button("🔄 Refresh Board");
        refreshBtn.setStyle(Styles.BTN_SECONDARY);
        refreshBtn.setOnAction(e -> {
            // Rebuild pane by re-triggering same method via sidebar action
        });

        pane.getChildren().addAll(heading, capacityLabel, board, refreshBtn);

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    // ─────────────────────────────────────────────────────────
    //  Issue Ticket
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildIssueTicketPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("Issue Parking Ticket");
        heading.setStyle(Styles.TITLE);

        VBox card = new VBox(16);
        card.setStyle(Styles.CARD_BG);
        card.setPadding(new Insets(24));
        card.setMaxWidth(520);

        // Check capacity first
        if (spotDAO.countFreeSpots() == 0) {
            Label fullMsg = new Label("⚠  Parking lot is FULL. Cannot issue new tickets.");
            fullMsg.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 14px; -fx-font-weight: bold;");
            card.getChildren().add(fullMsg);
            pane.getChildren().addAll(heading, card);
            ScrollPane sp = new ScrollPane(pane);
            sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
            sp.setFitToWidth(true);
            return sp;
        }

        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(12);

        TextField licenseField = new TextField();
        licenseField.setPromptText("e.g. ABC-1234");
        licenseField.setStyle(Styles.TEXT_FIELD);
        licenseField.setPrefWidth(220);

        ComboBox<VehicleType> vehicleTypeCombo = new ComboBox<>(FXCollections.observableArrayList(VehicleType.values()));
        vehicleTypeCombo.setPromptText("Vehicle Type");
        vehicleTypeCombo.setStyle(Styles.COMBO_BOX);

        ComboBox<ParkingSpot> spotCombo = new ComboBox<>(
                FXCollections.observableArrayList(spotDAO.getFreeSpots()));
        spotCombo.setPromptText("Select Free Spot");
        spotCombo.setStyle(Styles.COMBO_BOX);
        spotCombo.setPrefWidth(220);

        form.add(new Label("License Plate:") {{ setStyle(Styles.LABEL); }}, 0, 0);
        form.add(licenseField, 1, 0);
        form.add(new Label("Vehicle Type:") {{ setStyle(Styles.LABEL); }}, 0, 1);
        form.add(vehicleTypeCombo, 1, 1);
        form.add(new Label("Parking Spot:") {{ setStyle(Styles.LABEL); }}, 0, 2);
        form.add(spotCombo, 1, 2);

        Button issueBtn = new Button("Issue Ticket");
        issueBtn.setStyle(Styles.BTN_PRIMARY);

        Label resultLabel = new Label();
        resultLabel.setWrapText(true);

        VBox ticketBox = new VBox(8);
        ticketBox.setStyle("-fx-background-color: #1E4D2B; -fx-background-radius: 8; -fx-padding: 16;");
        ticketBox.setVisible(false);

        issueBtn.setOnAction(e -> {
            String license = licenseField.getText().trim().toUpperCase();
            VehicleType vType = vehicleTypeCombo.getValue();
            ParkingSpot spot = spotCombo.getValue();

            if (license.isEmpty() || vType == null || spot == null) {
                resultLabel.setStyle(Styles.MSG_ERROR);
                resultLabel.setText("All fields are required.");
                return;
            }

            int vehicleId = vehicleDAO.getOrCreateVehicle(license, vType);
            if (vehicleId == -1) {
                resultLabel.setStyle(Styles.MSG_ERROR);
                resultLabel.setText("Error creating vehicle record.");
                return;
            }

            String ticketNumber = ticketDAO.createTicket(vehicleId, spot.getId());
            if (ticketNumber == null) {
                resultLabel.setStyle(Styles.MSG_ERROR);
                resultLabel.setText("Error issuing ticket.");
                return;
            }

            // Mark spot as occupied
            spotDAO.updateSpotAvailability(spot.getId(), false);

            // Show ticket receipt
            ticketBox.setVisible(true);
            ticketBox.getChildren().clear();
            ticketBox.getChildren().addAll(
                    new Label("✅  TICKET ISSUED") {{ setStyle("-fx-text-fill: #27AE60; -fx-font-size: 15px; -fx-font-weight: bold;"); }},
                    new Label("Ticket #:  " + ticketNumber) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("License:   " + license) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Vehicle:   " + vType) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Spot:      " + spot.getSpotNumber()) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Time:      " + java.time.LocalDateTime.now().toString().replace("T", " ").substring(0, 16)) {{ setStyle(Styles.VALUE_TEXT); }}
            );

            resultLabel.setText("");
            licenseField.clear();
            vehicleTypeCombo.setValue(null);
            spotCombo.setItems(FXCollections.observableArrayList(spotDAO.getFreeSpots()));
            spotCombo.setValue(null);
        });

        card.getChildren().addAll(form, issueBtn, resultLabel, ticketBox);
        pane.getChildren().addAll(heading, card);

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    // ─────────────────────────────────────────────────────────
    //  Scan Ticket
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildScanTicketPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("Scan / Process Ticket");
        heading.setStyle(Styles.TITLE);

        VBox card = new VBox(16);
        card.setStyle(Styles.CARD_BG);
        card.setPadding(new Insets(24));
        card.setMaxWidth(520);

        Label instructions = new Label("Enter the ticket number to look up the parking fee.");
        instructions.setStyle(Styles.LABEL);

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        TextField ticketField = new TextField();
        ticketField.setPromptText("Ticket number (e.g. TKT-...)");
        ticketField.setStyle(Styles.TEXT_FIELD);
        ticketField.setPrefWidth(280);
        Button scanBtn = new Button("Scan Ticket");
        scanBtn.setStyle(Styles.BTN_PRIMARY);
        row.getChildren().addAll(ticketField, scanBtn);

        Label statusLabel = new Label();
        statusLabel.setStyle(Styles.MSG_ERROR);

        VBox infoBox = new VBox(10);
        infoBox.setStyle("-fx-background-color: #243447; -fx-background-radius: 8; -fx-padding: 16;");
        infoBox.setVisible(false);

        scanBtn.setOnAction(e -> {
            String ticketNum = ticketField.getText().trim();
            if (ticketNum.isEmpty()) { statusLabel.setText("Enter a ticket number."); return; }

            ParkingTicket ticket = ticketDAO.getByTicketNumber(ticketNum);
            if (ticket == null) {
                statusLabel.setStyle(Styles.MSG_ERROR);
                statusLabel.setText("Ticket not found.");
                infoBox.setVisible(false);
                return;
            }

            if (ticket.getStatus().name().equals("PAID")) {
                statusLabel.setStyle(Styles.MSG_SUCCESS);
                statusLabel.setText("This ticket has already been paid.");
                infoBox.setVisible(false);
                return;
            }

            double fee = com.parkinglot.util.FeeCalculator.calculateUntilNow(ticket.getIssuedAt());

            infoBox.setVisible(true);
            infoBox.getChildren().clear();
            infoBox.getChildren().addAll(
                    new Label("🎫  Ticket Details") {{ setStyle(Styles.SECTION_HEADER); }},
                    new Label("Ticket #:   " + ticket.getTicketNumber()) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("License:    " + ticket.getLicenseNumber()) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Vehicle:    " + ticket.getVehicleType()) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Spot:       " + ticket.getSpotNumber() + "  (" + ticket.getFloorName() + ")") {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Issued At:  " + ticket.getIssuedAt().toString().replace("T"," ").substring(0,16)) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("─────────────────────────────") {{ setStyle("-fx-text-fill: #2E3D52;"); }},
                    new Label("Total Fee:  $" + String.format("%.2f", fee)) {{
                        setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #F39C12;");
                    }}
            );

            Button payBtn = new Button("Proceed to Payment  →");
            payBtn.setStyle(Styles.BTN_SUCCESS);
            final String tNum = ticket.getTicketNumber();
            payBtn.setOnAction(pe -> SceneManager.showPaymentScene(tNum));
            infoBox.getChildren().add(payBtn);

            statusLabel.setText("");
        });

        card.getChildren().addAll(instructions, row, statusLabel, infoBox);
        pane.getChildren().addAll(heading, card);

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    // ─────────────────────────────────────────────────────────
    //  Active Tickets Table
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildActiveTicketsPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("Active Tickets");
        heading.setStyle(Styles.TITLE);

        TableView<ParkingTicket> table = new TableView<>();
        table.setStyle(Styles.TABLE_VIEW);
        table.setPrefHeight(500);

        TableColumn<ParkingTicket, String> numCol = new TableColumn<>("Ticket #");
        numCol.setCellValueFactory(new PropertyValueFactory<>("ticketNumber")); numCol.setPrefWidth(170);

        TableColumn<ParkingTicket, String> licCol = new TableColumn<>("License");
        licCol.setCellValueFactory(new PropertyValueFactory<>("licenseNumber")); licCol.setPrefWidth(120);

        TableColumn<ParkingTicket, String> typeCol = new TableColumn<>("Vehicle");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType")); typeCol.setPrefWidth(110);

        TableColumn<ParkingTicket, String> spotCol = new TableColumn<>("Spot");
        spotCol.setCellValueFactory(new PropertyValueFactory<>("spotNumber")); spotCol.setPrefWidth(80);

        TableColumn<ParkingTicket, String> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floorName")); floorCol.setPrefWidth(110);

        TableColumn<ParkingTicket, String> issuedCol = new TableColumn<>("Issued At");
        issuedCol.setCellValueFactory(new PropertyValueFactory<>("issuedAt")); issuedCol.setPrefWidth(160);

        table.getColumns().addAll(numCol, licCol, typeCol, spotCol, floorCol, issuedCol);
        table.setItems(FXCollections.observableArrayList(ticketDAO.getAllActiveTickets()));

        Button processBtn = new Button("Process Payment for Selected");
        processBtn.setStyle(Styles.BTN_PRIMARY);
        Label msg = new Label();
        msg.setStyle(Styles.MSG_ERROR);

        processBtn.setOnAction(e -> {
            ParkingTicket selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { msg.setText("Select a ticket first."); return; }
            SceneManager.showPaymentScene(selected.getTicketNumber());
        });

        pane.getChildren().addAll(heading, table, processBtn, msg);

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    public Scene getScene() { return scene; }
}
