package com.parkinglot.ui;

import com.parkinglot.dao.PaymentDAO;
import com.parkinglot.dao.ParkingSpotDAO;
import com.parkinglot.dao.ParkingTicketDAO;
import com.parkinglot.enums.AccountRole;
import com.parkinglot.enums.PaymentStatus;
import com.parkinglot.enums.PaymentType;
import com.parkinglot.models.Account;
import com.parkinglot.models.Payment;
import com.parkinglot.models.ParkingTicket;
import com.parkinglot.util.FeeCalculator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class PaymentScene {

    private final Scene scene;
    private final ParkingTicketDAO ticketDAO = new ParkingTicketDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final ParkingSpotDAO spotDAO = new ParkingSpotDAO();

    public PaymentScene(String ticketNumber) {
        BorderPane root = new BorderPane();
        root.setStyle(Styles.ROOT_BG);
        root.setTop(buildTopBar());

        ParkingTicket ticket = ticketDAO.getByTicketNumber(ticketNumber);

        if (ticket == null) {
            VBox err = new VBox(new Label("Ticket not found.") {{ setStyle(Styles.MSG_ERROR); }});
            err.setAlignment(Pos.CENTER);
            root.setCenter(err);
        } else if (!ticket.getStatus().name().equals("ACTIVE")) {
            VBox paid = new VBox(16);
            paid.setAlignment(Pos.CENTER);
            paid.getChildren().add(new Label("✅  This ticket has already been paid.") {{
                setStyle("-fx-text-fill: #27AE60; -fx-font-size: 16px; -fx-font-weight: bold;");
            }});
            Button backBtn = new Button("← Back to Dashboard");
            backBtn.setStyle(Styles.BTN_SECONDARY);
            backBtn.setOnAction(e -> goBack());
            paid.getChildren().add(backBtn);
            root.setCenter(paid);
        } else {
            root.setCenter(buildPaymentPane(ticket));
        }

        scene = new Scene(root, 900, 650);
    }

    private HBox buildTopBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 20, 14, 20));
        bar.setStyle("-fx-background-color: #111D2B; -fx-border-color: #1E3048; -fx-border-width: 0 0 1 0;");

        Text logo = new Text("🅿  Process Payment");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #FFFFFF; -fx-font-family: 'Courier New';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backBtn = new Button("← Back");
        backBtn.setStyle(Styles.BTN_SECONDARY);
        backBtn.setOnAction(e -> goBack());

        bar.getChildren().addAll(logo, spacer, backBtn);
        return bar;
    }

    private ScrollPane buildPaymentPane(ParkingTicket ticket) {
        HBox mainRow = new HBox(24);
        mainRow.setPadding(new Insets(28));
        mainRow.setAlignment(Pos.TOP_CENTER);

        double fee = FeeCalculator.calculateUntilNow(ticket.getIssuedAt());

        // ── Left: Ticket Summary ──────────────────────────────
        VBox summaryCard = new VBox(14);
        summaryCard.setStyle(Styles.CARD_BG);
        summaryCard.setPadding(new Insets(24));
        summaryCard.setPrefWidth(340);

        Text sumTitle = new Text("🎫  Ticket Summary");
        sumTitle.setStyle(Styles.SECTION_HEADER);

        summaryCard.getChildren().addAll(
                sumTitle,
                infoRow("Ticket #",  ticket.getTicketNumber()),
                infoRow("License",   ticket.getLicenseNumber()),
                infoRow("Vehicle",   ticket.getVehicleType()),
                infoRow("Spot",      ticket.getSpotNumber() + " (" + ticket.getFloorName() + ")"),
                infoRow("Issued At", ticket.getIssuedAt().toString().replace("T"," ").substring(0,16)),
                new Separator() {{ setStyle("-fx-background-color: #2E3D52;"); }}
        );

        Label feeLabel = new Label("Total Fee:  $" + String.format("%.2f", fee));
        feeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #F39C12;");
        summaryCard.getChildren().add(feeLabel);

        // ── Right: Payment Method ─────────────────────────────
        VBox payCard = new VBox(16);
        payCard.setStyle(Styles.CARD_BG);
        payCard.setPadding(new Insets(24));
        payCard.setPrefWidth(380);

        Text payTitle = new Text("💳  Select Payment Method");
        payTitle.setStyle(Styles.SECTION_HEADER);

        ToggleGroup group = new ToggleGroup();
        RadioButton cashBtn   = new RadioButton("💵  Cash");
        RadioButton cardBtn   = new RadioButton("💳  Credit Card");
        cashBtn.setToggleGroup(group); cashBtn.setSelected(true);
        cardBtn.setToggleGroup(group);
        cashBtn.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 14px;");
        cardBtn.setStyle("-fx-text-fill: #B0BEC5; -fx-font-size: 14px;");

        HBox radioRow = new HBox(20, cashBtn, cardBtn);

        // Cash panel
        VBox cashPanel = new VBox(10);
        TextField cashField = new TextField();
        cashField.setPromptText("Amount tendered ($)");
        cashField.setStyle(Styles.TEXT_FIELD);
        Label changeLabel = new Label();
        changeLabel.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 14px; -fx-font-weight: bold;");

        cashField.textProperty().addListener((obs, ov, nv) -> {
            try {
                double tendered = Double.parseDouble(nv.trim());
                double change = tendered - fee;
                if (change >= 0) changeLabel.setText("Change: $" + String.format("%.2f", change));
                else changeLabel.setText("Amount insufficient.");
            } catch (NumberFormatException ex) {
                changeLabel.setText("");
            }
        });

        cashPanel.getChildren().addAll(
                new Label("Cash Tendered:") {{ setStyle(Styles.LABEL); }},
                cashField, changeLabel
        );

        // Card panel
        VBox cardPanel = new VBox(10);
        cardPanel.setVisible(false);
        TextField nameField = new TextField();
        nameField.setPromptText("Name on card");
        nameField.setStyle(Styles.TEXT_FIELD);
        TextField cardNumField = new TextField();
        cardNumField.setPromptText("Card number (16 digits)");
        cardNumField.setStyle(Styles.TEXT_FIELD);
        TextField expiryField = new TextField();
        expiryField.setPromptText("MM/YY");
        expiryField.setStyle(Styles.TEXT_FIELD);
        expiryField.setPrefWidth(100);
        TextField cvvField = new TextField();
        cvvField.setPromptText("CVV");
        cvvField.setStyle(Styles.TEXT_FIELD);
        cvvField.setPrefWidth(80);
        HBox expiryRow = new HBox(10, expiryField, cvvField);

        cardPanel.getChildren().addAll(
                new Label("Name on Card:") {{ setStyle(Styles.LABEL); }}, nameField,
                new Label("Card Number:") {{ setStyle(Styles.LABEL); }}, cardNumField,
                new Label("Expiry / CVV:") {{ setStyle(Styles.LABEL); }}, expiryRow
        );

        // Toggle panels based on radio selection
        group.selectedToggleProperty().addListener((obs, ov, nv) -> {
            boolean isCash = nv == cashBtn;
            cashPanel.setVisible(isCash);
            cardPanel.setVisible(!isCash);
        });

        Button confirmBtn = new Button("Confirm Payment  ✓");
        confirmBtn.setStyle(Styles.BTN_SUCCESS);
        confirmBtn.setPrefWidth(300);

        Label resultLabel = new Label();
        resultLabel.setWrapText(true);

        VBox successBox = new VBox(12);
        successBox.setStyle("-fx-background-color: #1E4D2B; -fx-background-radius: 8; -fx-padding: 20;");
        successBox.setVisible(false);

        confirmBtn.setOnAction(e -> {
            boolean isCash = cashBtn.isSelected();
            Payment payment = new Payment(ticket.getId(), fee,
                    isCash ? PaymentType.CASH : PaymentType.CREDIT_CARD);

            if (isCash) {
                String cashText = cashField.getText().trim();
                if (cashText.isEmpty()) {
                    resultLabel.setStyle(Styles.MSG_ERROR);
                    resultLabel.setText("Enter the cash amount tendered.");
                    return;
                }
                try {
                    double tendered = Double.parseDouble(cashText);
                    if (tendered < fee) {
                        resultLabel.setStyle(Styles.MSG_ERROR);
                        resultLabel.setText("Insufficient cash. Fee is $" + String.format("%.2f", fee));
                        return;
                    }
                    payment.setCashTendered(tendered);
                } catch (NumberFormatException ex) {
                    resultLabel.setStyle(Styles.MSG_ERROR);
                    resultLabel.setText("Enter a valid number.");
                    return;
                }
            } else {
                String name = nameField.getText().trim();
                String cardNum = cardNumField.getText().trim();
                if (name.isEmpty() || cardNum.isEmpty()) {
                    resultLabel.setStyle(Styles.MSG_ERROR);
                    resultLabel.setText("All card fields are required.");
                    return;
                }
                if (!cardNum.matches("\\d{16}")) {
                    resultLabel.setStyle(Styles.MSG_ERROR);
                    resultLabel.setText("Card number must be 16 digits.");
                    return;
                }
                payment.setCardName(name);
            }

            payment.setStatus(PaymentStatus.COMPLETED);
            paymentDAO.savePayment(payment);
            ticketDAO.markAsPaid(ticket.getId(), fee);
            spotDAO.updateSpotAvailability(ticket.getSpotId(), true);

            resultLabel.setText("");
            confirmBtn.setVisible(false);

            successBox.setVisible(true);
            successBox.getChildren().clear();
            successBox.getChildren().addAll(
                    new Label("✅  Payment Successful!") {{
                        setStyle("-fx-text-fill: #27AE60; -fx-font-size: 18px; -fx-font-weight: bold;");
                    }},
                    new Label("Amount Paid: $" + String.format("%.2f", fee)) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Method: " + (isCash ? "Cash" : "Credit Card")) {{ setStyle(Styles.VALUE_TEXT); }},
                    new Label("Spot " + ticket.getSpotNumber() + " is now available.") {{
                        setStyle("-fx-text-fill: #7F8C9A; -fx-font-size: 12px;");
                    }}
            );

            Button doneBtn = new Button("← Back to Dashboard");
            doneBtn.setStyle(Styles.BTN_SECONDARY);
            doneBtn.setOnAction(de -> goBack());
            successBox.getChildren().add(doneBtn);
        });

        payCard.getChildren().addAll(payTitle, radioRow, cashPanel, cardPanel,
                new Separator() {{ setStyle("-fx-background-color: #2E3D52;"); }},
                confirmBtn, resultLabel, successBox);

        mainRow.getChildren().addAll(summaryCard, payCard);

        ScrollPane sp = new ScrollPane(mainRow);
        sp.setStyle("-fx-background: #1A2332; -fx-background-color: #1A2332;");
        sp.setFitToWidth(true);
        return sp;
    }

    private HBox infoRow(String label, String value) {
        HBox row = new HBox(8);
        Label lbl = new Label(label + ":");
        lbl.setStyle(Styles.LABEL);
        lbl.setMinWidth(80);
        Label val = new Label(value);
        val.setStyle(Styles.VALUE_TEXT);
        row.getChildren().addAll(lbl, val);
        return row;
    }

    private void goBack() {
        Account acc = SceneManager.getLoggedInAccount();
        if (acc != null && acc.getRole() == AccountRole.ADMIN) {
            SceneManager.showAdminDashboard();
        } else {
            SceneManager.showAttendantDashboard();
        }
    }

    public Scene getScene() { return scene; }
}
