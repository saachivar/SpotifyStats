module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires org.jsoup;
    requires java.desktop;
    requires javafx.graphics;


    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
}