module ParkingLotSystem {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    opens com.parkinglot to javafx.graphics, javafx.controls;
    opens com.parkinglot.ui to javafx.graphics, javafx.controls;
    opens com.parkinglot.models to javafx.base;
}