module skullian.binarysearchinator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;
    requires javafx.web;

    requires java.desktop;

    requires org.kordamp.ikonli.javafx;
    requires java.logging;

    opens skullian.binarysearchinator to javafx.fxml;
    exports skullian.binarysearchinator;
}