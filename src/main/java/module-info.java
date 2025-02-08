module org.example.vetorrally {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.vetorrally to javafx.fxml;
    exports org.example.vetorrally;
}