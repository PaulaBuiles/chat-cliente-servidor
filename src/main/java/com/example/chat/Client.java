package com.example.chat;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al crear el cliente");
            close(socket,bufferedReader,bufferedWriter);
        }
    }

    //Funcion para mandar mensajes al cliente:
    public void sendServerMessage(String message){
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine(); // Para dar a entender al cliente que es el final del mensaje
            bufferedWriter.flush();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al mandarle el mensaje al cliente");
            close(socket,bufferedReader,bufferedWriter);
        }
    }

    //Funcion para recibir mensajes del servidor: (Lo debemos ejecutar dentro de un hilo para que no se bloquee el programa esperando mensajes del cliente)
    public void receiveServerMessage(VBox vBox){
        new Thread(() -> {
            while (socket.isConnected()){
                try {
                    String clientMessage = bufferedReader.readLine();
                    ServerController.addLabel(clientMessage,vBox);
                }catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error al recibir mensajes del cliente");
                    close(socket,bufferedReader,bufferedWriter);
                    break;
                }
            }
        }).start(); //ejecuta subprocesos
    }


    //Funcion para cerrar el flujo, asi no gastamos recursos:
    public void close(Socket socket, BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
