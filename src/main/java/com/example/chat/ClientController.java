
package com.example.chat;

import java.io.IOException;
import java.net.Socket;
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

public class ClientController implements Initializable {


        @FXML
        private TextField introducir_mensaje;

        @FXML
        private ScrollPane scrollPane_mensajes;

        @FXML
        private Button sendButton;

        @FXML
        private VBox vbox_mensajes;

        private Client client;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

            try {
                client = new Client(new Socket("localhost",5050));
                System.out.println("Nos conectamos con el servidor");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al inicializar client");
            }

            vbox_mensajes.heightProperty().addListener((observableValue, oldNumber, newNumber) -> scrollPane_mensajes.setVvalue((Double)newNumber ));

            client.receiveServerMessage(vbox_mensajes);

            //Funcion para el boton enviar:
            //En el handle basicamente son solo propiedades de estetica y diseño.
            sendButton.setOnAction(actionEvent -> {
                String messageToSend = introducir_mensaje.getText();

                if(!messageToSend.isEmpty()) { //!messageToSend... significa que si no esta vacio (es una expresion logica para "no")
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

                    client.sendServerMessage(messageToSend);
                    introducir_mensaje.clear();
                }
            });

        }

        public static void addLabel(String serverMessage, VBox vbox){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(15,15,15,10));

            Text message = new Text(serverMessage);
            TextFlow textFlow = new TextFlow(message);

            textFlow.setStyle("-fx-background-color: rgb(233,233,255);" + "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5,10,5,10));

            hBox.getChildren().add(textFlow);

            //Usamos platform para hacer el subproceso de agregar el hbox (actualizar la interfaz grafica)
            Platform.runLater(() -> vbox.getChildren().add(hBox));

        }


}
