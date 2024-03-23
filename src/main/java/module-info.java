module skullian.binarysearchinator {
    // JavaFX
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.desktop;
    requires java.logging;
    requires java.sql;
    requires com.google.gson;

    requires org.yaml.snakeyaml;
    requires toml4j;

    requires com.zaxxer.hikari;
    requires org.xerial.sqlitejdbc;
    requires org.jetbrains.annotations;
    requires org.slf4j;

    // JavaFX
    opens skullian.binarysearchinator to javafx.fxml;
    exports skullian.binarysearchinator;
    exports skullian.binarysearchinator.control;
    opens skullian.binarysearchinator.control to javafx.fxml;
    exports skullian.binarysearchinator.control.old;
    opens skullian.binarysearchinator.control.old to javafx.fxml;
}