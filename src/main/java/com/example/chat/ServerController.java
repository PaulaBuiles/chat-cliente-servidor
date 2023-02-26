
package com.example.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ServerController implements Initializable {


    @FXML
    private TextField introducir_mensaje;

    @FXML
    private ScrollPane scrollPane_mensajes;

    @FXML
    private Button sendButton;

    @FXML
    private VBox vbox_mensajes;

    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            server = new Server(new ServerSocket(5050));
            System.out.println("Nos conectamos con el cliente");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al inicializar server");
        }

        vbox_mensajes.heightProperty().addListener((observableValue, oldNumber, newNumber) -> scrollPane_mensajes.setVvalue((Double)newNumber ));


        server.receiveClientMessage(vbox_mensajes);

        sendButton.setOnAction(actionEvent -> {
            String messageToSend = introducir_mensaje.getText();
            if(!messageToSend.isEmpty()) {

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(15,15,15,10));

                Text message = new Text(messageToSend);
                TextFlow textFlow = new TextFlow(message);

                textFlow.setStyle("-fx-color : rgb(239,242,255);" + "-fx-background-color: rgb(15,125,242);" + "-fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5,10,5,10));
                message.setFill(Color.color(0.934,0.945,0.996));

                hBox.getChildren().add(textFlow);
                vbox_mensajes.getChildren().add(hBox);

                server.sendClientMessage(messageToSend);
                introducir_mensaje.clear();
            }
        });

    }

    public static void addLabel(String clientMessage, VBox vbox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(15,15,15,10));

        Text message = new Text(clientMessage);
        TextFlow textFlow = new TextFlow(message);

        textFlow.setStyle("-fx-background-color: rgb(233,233,255);" + "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));

        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vbox.getChildren().add(hBox));

    }


}
