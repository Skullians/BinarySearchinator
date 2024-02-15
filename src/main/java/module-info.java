module skullian.binarysearchinator {
    // JavaFX
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // Miscellaneous
    requires java.desktop;
    requires java.logging;
    requires java.sql;

    // File Parsing
    requires org.yaml.snakeyaml;
    requires org.json;
    requires toml4j;

    // JavaFX
    opens skullian.binarysearchinator to javafx.fxml;
    exports skullian.binarysearchinator;
    exports skullian.binarysearchinator.control;
    opens skullian.binarysearchinator.control to javafx.fxml;
}