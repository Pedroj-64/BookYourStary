module co.edu.uniquindio.poo.bookyourstary {
    requires javafx.fxml;
    requires jakarta.mail;
    requires com.google.zxing;
    requires com.google.gson;
    requires java.desktop;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires org.apache.commons.codec;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;
    requires javafx.base;
    requires com.sun.xml.bind;
    requires annotations;

    opens co.edu.uniquindio.poo.bookyourstary to javafx.fxml;

    exports co.edu.uniquindio.poo.bookyourstary;

    opens co.edu.uniquindio.poo.bookyourstary.viewController to javafx.fxml;

    exports co.edu.uniquindio.poo.bookyourstary.viewController;

    opens co.edu.uniquindio.poo.bookyourstary.model to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.model;

    opens co.edu.uniquindio.poo.bookyourstary.model.enums to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.model.enums;

    opens co.edu.uniquindio.poo.bookyourstary.model.factory to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.model.factory;

    opens co.edu.uniquindio.poo.bookyourstary.model.strategy to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.model.strategy;

    opens co.edu.uniquindio.poo.bookyourstary.service to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.service;

    opens co.edu.uniquindio.poo.bookyourstary.util to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.util;

    opens co.edu.uniquindio.poo.bookyourstary.config to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.config;

    opens co.edu.uniquindio.poo.bookyourstary.repository to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.repository;

    opens co.edu.uniquindio.poo.bookyourstary.service.observer to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.service.observer;

    opens co.edu.uniquindio.poo.bookyourstary.internalControllers to javafx.base, com.google.gson;

    exports co.edu.uniquindio.poo.bookyourstary.internalControllers;

}