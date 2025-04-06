module co.edu.uniquindio.poo.bookyourstary {
    requires javafx.fxml;
    requires jakarta.mail;
    requires com.google.zxing;
    requires com.google.gson;
    requires java.desktop;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens co.edu.uniquindio.poo.bookyourstary to javafx.fxml;
    exports co.edu.uniquindio.poo.bookyourstary;

    opens co.edu.uniquindio.poo.bookyourstary.viewController to javafx.fxml;
    exports co.edu.uniquindio.poo.bookyourstary.viewController;

    opens co.edu.uniquindio.poo.bookyourstary.model to javafx.base, com.google.gson;
    exports co.edu.uniquindio.poo.bookyourstary.model;

    opens co.edu.uniquindio.poo.bookyourstary.service to javafx.base, com.google.gson;
    exports co.edu.uniquindio.poo.bookyourstary.service;

    
}