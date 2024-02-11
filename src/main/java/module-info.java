module skullian.binarysearchinator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;

    requires org.kordamp.ikonli.javafx;

    opens skullian.binarysearchinator to javafx.fxml;
    exports skullian.binarysearchinator;
}