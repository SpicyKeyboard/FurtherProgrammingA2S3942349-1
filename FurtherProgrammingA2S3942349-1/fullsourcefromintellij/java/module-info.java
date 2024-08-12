module com.example.fprogass2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.fprogass2 to javafx.fxml;
    exports com.example.fprogass2;
}