module com.example.chat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.chat to javafx.fxml;
    exports com.example.chat;

}