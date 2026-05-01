package com.parkinglot.ui;

import com.parkinglot.dao.*;
import com.parkinglot.enums.ParkingSpotType;
import com.parkinglot.models.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

public class AdminDashboardScene {

    private final Scene scene;
    private final AccountDAO accountDAO = new AccountDAO();
    private final ParkingFloorDAO floorDAO = new ParkingFloorDAO();
    private final ParkingSpotDAO spotDAO = new ParkingSpotDAO();
    private final ParkingTicketDAO ticketDAO = new ParkingTicketDAO();

    public AdminDashboardScene() {
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT_BG);

        // ── Top Bar ───────────────────────────────────────────
        root.setTop(buildTopBar());

        // ── Sidebar ───────────────────────────────────────────
        VBox sidebar = buildSidebar(root);
        root.setLeft(sidebar);

        // ── Default Content: Overview ─────────────────────────
        root.setCenter(buildOverviewPane());

        scene = new Scene(root, 1100, 700);
    }

    // ─────────────────────────────────────────────────────────
    //  Top Bar
    // ─────────────────────────────────────────────────────────
    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 20, 14, 20));
        bar.setStyle("-fx-background-color: #111D2B; -fx-border-color: #1E3048; " +
                "-fx-border-width: 0 0 1 0;");

        Text logo = new Text("🅿  Parking Lot System");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #FFFFFF; " +
                "-fx-font-family: 'Courier New';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Account acc = SceneManager.getLoggedInAccount();
        Label userLabel = new Label("👤 " + acc.getUsername() + "  |  ADMIN");
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
        menuLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #3D5166; -fx-font-weight: bold; " +
                "-fx-padding: 0 0 8 8;");

        String[] items = {"📊 Overview", "🏢 Floors & Spots", "👷 Attendants", "🎫 All Tickets"};
        Button[] btns = new Button[items.length];

        for (int i = 0; i < items.length; i++) {
            final int idx = i;
            btns[i] = new Button(items[i]);
            btns[i].setMaxWidth(Double.MAX_VALUE);
            btns[i].setStyle(idx == 0 ? Styles.NAV_BTN_ACTIVE : Styles.NAV_BTN);
        }

        btns[0].setOnAction(e -> {
            root.setCenter(buildOverviewPane());
            setActive(btns, 0);
        });
        btns[1].setOnAction(e -> {
            root.setCenter(buildFloorsPane());
            setActive(btns, 1);
        });
        btns[2].setOnAction(e -> {
            root.setCenter(buildAttendantsPane());
            setActive(btns, 2);
        });
        btns[3].setOnAction(e -> {
            root.setCenter(buildTicketsPane());
            setActive(btns, 3);
        });

        sidebar.getChildren().add(menuLabel);
        sidebar.getChildren().addAll(btns);
        return sidebar;
    }

    private void setActive(Button[] btns, int active) {
        for (int i = 0; i < btns.length; i++)
            btns[i].setStyle(i == active ? Styles.NAV_BTN_ACTIVE : Styles.NAV_BTN);
    }

    // ─────────────────────────────────────────────────────────
    //  Overview Pane
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildOverviewPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("Dashboard Overview");
        heading.setStyle(Styles.TITLE);

        int total = spotDAO.countTotalSpots();
        int free  = spotDAO.countFreeSpots();
        int active = ticketDAO.getAllActiveTickets().size();
        int attendants = accountDAO.getAllAttendants().size();

        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                statCard("Total Spots",    String.valueOf(total),    "#2E86AB"),
                statCard("Free Spots",     String.valueOf(free),     "#27AE60"),
                statCard("Active Tickets", String.valueOf(active),   "#F39C12"),
                statCard("Attendants",     String.valueOf(attendants),"#9B59B6")
        );

        // Display Board
        Text boardTitle = new Text("Live Display Board");
        boardTitle.setStyle(Styles.SECTION_HEADER);

        GridPane board = new GridPane();
        board.setHgap(12);
        board.setVgap(12);
        board.setPadding(new Insets(16));
        board.setStyle(Styles.CARD_BG);

        ParkingSpotType[] types = ParkingSpotType.values();
        for (int i = 0; i < types.length; i++) {
            int freeCount = spotDAO.countFreeSpotsByType(types[i]);
            VBox tile = new VBox(4);
            tile.setAlignment(Pos.CENTER);
            tile.setPrefWidth(140);
            tile.setPrefHeight(70);
            tile.setStyle(freeCount > 0 ? Styles.SPOT_FREE : Styles.SPOT_OCCUPIED);
            Label typeLabel = new Label(types[i].name());
            typeLabel.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 11px; -fx-font-weight: bold;");
            Label countLabel = new Label(freeCount + " FREE");
            countLabel.setStyle("-fx-text-fill: " + (freeCount > 0 ? "#27AE60" : "#E74C3C") +
                    "; -fx-font-size: 18px; -fx-font-weight: bold;");
            tile.getChildren().addAll(typeLabel, countLabel);
            board.add(tile, i % 3, i / 3);
        }

        boolean isFull = free == 0;
        if (isFull) {
            Label fullMsg = new Label("⚠  PARKING LOT IS FULL");
            fullMsg.setStyle("-fx-background-color: #4D1E1E; -fx-text-fill: #E74C3C; " +
                    "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 20; " +
                    "-fx-background-radius: 8;");
            pane.getChildren().addAll(heading, stats, boardTitle, board, fullMsg);
        } else {
            pane.getChildren().addAll(heading, stats, boardTitle, board);
        }

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    private VBox statCard(String label, String value, String color) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(160);
        card.setPrefHeight(90);
        card.setStyle("-fx-background-color: #243447; -fx-background-radius: 10; " +
                "-fx-border-color: " + color + "; -fx-border-radius: 10; " +
                "-fx-border-width: 0 0 3 0;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #7F8C9A;");
        card.getChildren().addAll(val, lbl);
        return card;
    }

    // ─────────────────────────────────────────────────────────
    //  Floors & Spots Pane
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildFloorsPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("Floors & Parking Spots");
        heading.setStyle(Styles.TITLE);

        // ── Add Floor ─────────────────────────────────────────
        VBox addFloorCard = new VBox(12);
        addFloorCard.setStyle(Styles.CARD_BG);
        addFloorCard.setPadding(new Insets(16));

        Text floorTitle = new Text("Add New Floor");
        floorTitle.setStyle(Styles.SECTION_HEADER);

        HBox floorRow = new HBox(10);
        floorRow.setAlignment(Pos.CENTER_LEFT);
        TextField floorNameField = new TextField();
        floorNameField.setPromptText("Floor name (e.g. Floor 3)");
        floorNameField.setStyle(Styles.TEXT_FIELD);
        floorNameField.setPrefWidth(200);
        TextField floorNumField = new TextField();
        floorNumField.setPromptText("Floor number");
        floorNumField.setStyle(Styles.TEXT_FIELD);
        floorNumField.setPrefWidth(120);
        Button addFloorBtn = new Button("Add Floor");
        addFloorBtn.setStyle(Styles.BTN_SUCCESS);
        Label floorMsg = new Label();
        floorMsg.setStyle(Styles.MSG_SUCCESS);

        addFloorBtn.setOnAction(e -> {
            try {
                String name = floorNameField.getText().trim();
                int num = Integer.parseInt(floorNumField.getText().trim());
                if (name.isEmpty()) { floorMsg.setStyle(Styles.MSG_ERROR); floorMsg.setText("Name required."); return; }
                if (floorDAO.addFloor(name, num)) {
                    floorMsg.setStyle(Styles.MSG_SUCCESS);
                    floorMsg.setText("Floor added successfully!");
                    floorNameField.clear(); floorNumField.clear();
                } else {
                    floorMsg.setStyle(Styles.MSG_ERROR);
                    floorMsg.setText("Failed — floor number may already exist.");
                }
            } catch (NumberFormatException ex) {
                floorMsg.setStyle(Styles.MSG_ERROR);
                floorMsg.setText("Floor number must be an integer.");
            }
        });

        floorRow.getChildren().addAll(floorNameField, floorNumField, addFloorBtn);
        addFloorCard.getChildren().addAll(floorTitle, floorRow, floorMsg);

        // ── Add Spot ──────────────────────────────────────────
        VBox addSpotCard = new VBox(12);
        addSpotCard.setStyle(Styles.CARD_BG);
        addSpotCard.setPadding(new Insets(16));

        Text spotTitle = new Text("Add New Parking Spot");
        spotTitle.setStyle(Styles.SECTION_HEADER);

        HBox spotRow = new HBox(10);
        spotRow.setAlignment(Pos.CENTER_LEFT);

        TextField spotNumField = new TextField();
        spotNumField.setPromptText("Spot number (e.g. G-08)");
        spotNumField.setStyle(Styles.TEXT_FIELD);
        spotNumField.setPrefWidth(180);

        ComboBox<ParkingFloor> floorCombo = new ComboBox<>(
                FXCollections.observableArrayList(floorDAO.getAllFloors()));
        floorCombo.setPromptText("Select Floor");
        floorCombo.setStyle(Styles.COMBO_BOX);

        ComboBox<ParkingSpotType> typeCombo = new ComboBox<>(
                FXCollections.observableArrayList(ParkingSpotType.values()));
        typeCombo.setPromptText("Spot Type");
        typeCombo.setStyle(Styles.COMBO_BOX);

        Button addSpotBtn = new Button("Add Spot");
        addSpotBtn.setStyle(Styles.BTN_SUCCESS);
        Label spotMsg = new Label();
        spotMsg.setStyle(Styles.MSG_SUCCESS);

        addSpotBtn.setOnAction(e -> {
            String num = spotNumField.getText().trim();
            ParkingFloor floor = floorCombo.getValue();
            ParkingSpotType type = typeCombo.getValue();
            if (num.isEmpty() || floor == null || type == null) {
                spotMsg.setStyle(Styles.MSG_ERROR);
                spotMsg.setText("All fields required.");
                return;
            }
            if (spotDAO.addSpot(num, floor.getId(), type)) {
                spotMsg.setStyle(Styles.MSG_SUCCESS);
                spotMsg.setText("Spot added successfully!");
                spotNumField.clear();
            } else {
                spotMsg.setStyle(Styles.MSG_ERROR);
                spotMsg.setText("Failed — spot number may already exist on this floor.");
            }
        });

        spotRow.getChildren().addAll(spotNumField, floorCombo, typeCombo, addSpotBtn);
        addSpotCard.getChildren().addAll(spotTitle, spotRow, spotMsg);

        // ── Spots Table ───────────────────────────────────────
        Text tableTitle = new Text("All Parking Spots");
        tableTitle.setStyle(Styles.SECTION_HEADER);

        TableView<ParkingSpot> table = buildSpotsTable();

        Button deleteSpotBtn = new Button("Delete Selected Spot");
        deleteSpotBtn.setStyle(Styles.BTN_DANGER);
        Label deleteMsg = new Label();
        deleteMsg.setStyle(Styles.MSG_ERROR);

        deleteSpotBtn.setOnAction(e -> {
            ParkingSpot selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { deleteMsg.setText("Select a spot first."); return; }
            if (!selected.isFree()) { deleteMsg.setText("Cannot delete an occupied spot."); return; }
            if (spotDAO.deleteSpot(selected.getId())) {
                table.setItems(FXCollections.observableArrayList(spotDAO.getAllSpots()));
                deleteMsg.setStyle(Styles.MSG_SUCCESS);
                deleteMsg.setText("Spot deleted.");
            }
        });

        pane.getChildren().addAll(heading, addFloorCard, addSpotCard, tableTitle, table, deleteSpotBtn, deleteMsg);

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    private TableView<ParkingSpot> buildSpotsTable() {
        TableView<ParkingSpot> table = new TableView<>();
        table.setStyle(Styles.TABLE_VIEW);
        table.setPrefHeight(300);

        TableColumn<ParkingSpot, String> numCol = new TableColumn<>("Spot #");
        numCol.setCellValueFactory(new PropertyValueFactory<>("spotNumber"));
        numCol.setPrefWidth(100);

        TableColumn<ParkingSpot, Integer> floorCol = new TableColumn<>("Floor ID");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floorId"));
        floorCol.setPrefWidth(80);

        TableColumn<ParkingSpot, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(130);

        TableColumn<ParkingSpot, Boolean> freeCol = new TableColumn<>("Status");
        freeCol.setCellValueFactory(new PropertyValueFactory<>("isFree"));
        freeCol.setPrefWidth(100);
        freeCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item ? "FREE" : "OCCUPIED");
                    setStyle(item ? "-fx-text-fill: #27AE60; -fx-font-weight: bold;"
                            : "-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
                }
            }
        });

        table.getColumns().addAll(numCol, floorCol, typeCol, freeCol);
        table.setItems(FXCollections.observableArrayList(spotDAO.getAllSpots()));
        return table;
    }

    // ─────────────────────────────────────────────────────────
    //  Attendants Pane
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildAttendantsPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("Manage Attendants");
        heading.setStyle(Styles.TITLE);

        // Add attendant form
        VBox formCard = new VBox(12);
        formCard.setStyle(Styles.CARD_BG);
        formCard.setPadding(new Insets(20));

        Text formTitle = new Text("Add New Attendant");
        formTitle.setStyle(Styles.SECTION_HEADER);

        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(10);

        TextField unField = new TextField(); unField.setPromptText("Username"); unField.setStyle(Styles.TEXT_FIELD); unField.setPrefWidth(200);
        TextField emailField = new TextField(); emailField.setPromptText("Email"); emailField.setStyle(Styles.TEXT_FIELD); emailField.setPrefWidth(200);
        PasswordField pwField = new PasswordField(); pwField.setPromptText("Password"); pwField.setStyle(Styles.TEXT_FIELD); pwField.setPrefWidth(200);

        form.add(new Label("Username:") {{ setStyle(Styles.LABEL); }}, 0, 0);
        form.add(unField, 1, 0);
        form.add(new Label("Email:") {{ setStyle(Styles.LABEL); }}, 0, 1);
        form.add(emailField, 1, 1);
        form.add(new Label("Password:") {{ setStyle(Styles.LABEL); }}, 0, 2);
        form.add(pwField, 1, 2);

        Button addBtn = new Button("Add Attendant");
        addBtn.setStyle(Styles.BTN_SUCCESS);
        Label addMsg = new Label();

        // Attendants table
        TableView<Account> table = buildAccountsTable();

        addBtn.setOnAction(e -> {
            String un = unField.getText().trim();
            String em = emailField.getText().trim();
            String pw = pwField.getText().trim();
            if (un.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                addMsg.setStyle(Styles.MSG_ERROR); addMsg.setText("All fields required."); return;
            }
            if (accountDAO.usernameExists(un)) {
                addMsg.setStyle(Styles.MSG_ERROR); addMsg.setText("Username already exists."); return;
            }
            if (accountDAO.addAttendant(un, pw, em)) {
                addMsg.setStyle(Styles.MSG_SUCCESS); addMsg.setText("Attendant added!");
                unField.clear(); emailField.clear(); pwField.clear();
                table.setItems(FXCollections.observableArrayList(accountDAO.getAllAttendants()));
            } else {
                addMsg.setStyle(Styles.MSG_ERROR); addMsg.setText("Failed to add attendant.");
            }
        });

        formCard.getChildren().addAll(formTitle, form, addBtn, addMsg);

        Text tableTitle = new Text("Current Attendants");
        tableTitle.setStyle(Styles.SECTION_HEADER);

        Button deleteBtn = new Button("Remove Selected");
        deleteBtn.setStyle(Styles.BTN_DANGER);
        Label delMsg = new Label();

        deleteBtn.setOnAction(e -> {
            Account selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { delMsg.setStyle(Styles.MSG_ERROR); delMsg.setText("Select an attendant first."); return; }
            if (accountDAO.removeAccount(selected.getId())) {
                table.setItems(FXCollections.observableArrayList(accountDAO.getAllAttendants()));
                delMsg.setStyle(Styles.MSG_SUCCESS); delMsg.setText("Attendant removed.");
            }
        });

        pane.getChildren().addAll(heading, formCard, tableTitle, table, deleteBtn, delMsg);

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    private TableView<Account> buildAccountsTable() {
        TableView<Account> table = new TableView<>();
        table.setStyle(Styles.TABLE_VIEW);
        table.setPrefHeight(250);

        TableColumn<Account, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id")); idCol.setPrefWidth(50);

        TableColumn<Account, String> unCol = new TableColumn<>("Username");
        unCol.setCellValueFactory(new PropertyValueFactory<>("username")); unCol.setPrefWidth(150);

        TableColumn<Account, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email")); emailCol.setPrefWidth(200);

        TableColumn<Account, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role")); roleCol.setPrefWidth(100);

        TableColumn<Account, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status")); statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, unCol, emailCol, roleCol, statusCol);
        table.setItems(FXCollections.observableArrayList(accountDAO.getAllAttendants()));
        return table;
    }

    // ─────────────────────────────────────────────────────────
    //  Tickets Pane
    // ─────────────────────────────────────────────────────────
    private ScrollPane buildTicketsPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(28));

        Text heading = new Text("All Tickets");
        heading.setStyle(Styles.TITLE);

        TableView<ParkingTicket> table = buildTicketsTable();

        pane.getChildren().addAll(heading, table);

        ScrollPane sp = new ScrollPane(pane);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    private TableView<ParkingTicket> buildTicketsTable() {
        TableView<ParkingTicket> table = new TableView<>();
        table.setStyle(Styles.TABLE_VIEW);
        table.setPrefHeight(500);

        TableColumn<ParkingTicket, String> numCol = new TableColumn<>("Ticket #");
        numCol.setCellValueFactory(new PropertyValueFactory<>("ticketNumber")); numCol.setPrefWidth(160);

        TableColumn<ParkingTicket, String> licCol = new TableColumn<>("License");
        licCol.setCellValueFactory(new PropertyValueFactory<>("licenseNumber")); licCol.setPrefWidth(120);

        TableColumn<ParkingTicket, String> typeCol = new TableColumn<>("Vehicle");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType")); typeCol.setPrefWidth(100);

        TableColumn<ParkingTicket, String> spotCol = new TableColumn<>("Spot");
        spotCol.setCellValueFactory(new PropertyValueFactory<>("spotNumber")); spotCol.setPrefWidth(80);

        TableColumn<ParkingTicket, String> floorCol = new TableColumn<>("Floor");
        floorCol.setCellValueFactory(new PropertyValueFactory<>("floorName")); floorCol.setPrefWidth(100);

        TableColumn<ParkingTicket, String> issuedCol = new TableColumn<>("Issued At");
        issuedCol.setCellValueFactory(new PropertyValueFactory<>("issuedAt")); issuedCol.setPrefWidth(160);

        TableColumn<ParkingTicket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status")); statusCol.setPrefWidth(90);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    setStyle(item.equals("ACTIVE") ? "-fx-text-fill: #F39C12; -fx-font-weight: bold;"
                            : item.equals("PAID")   ? "-fx-text-fill: #27AE60; -fx-font-weight: bold;"
                            : "-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<ParkingTicket, Double> amtCol = new TableColumn<>("Paid ($)");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("paidAmount")); amtCol.setPrefWidth(80);

        table.getColumns().addAll(numCol, licCol, typeCol, spotCol, floorCol, issuedCol, statusCol, amtCol);
        table.setItems(FXCollections.observableArrayList(ticketDAO.getAllTickets()));
        return table;
    }

    public Scene getScene() { return scene; }
}
