module com.example.advertisementfirmcourseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.advertisementfirmcourseproject to javafx.fxml;
    exports com.example.advertisementfirmcourseproject;
    exports com.example.advertisementfirmcourseproject.LoginController;
    opens com.example.advertisementfirmcourseproject.LoginController to javafx.fxml;
    exports com.example.advertisementfirmcourseproject.UserControllers;
    opens com.example.advertisementfirmcourseproject.UserControllers to javafx.fxml;
}