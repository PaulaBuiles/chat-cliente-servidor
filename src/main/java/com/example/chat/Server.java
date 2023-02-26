package com.example.chat;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket; //Tiene un flujo de salida para escribir a quien nos conectamos y uno de entrada para leer lo que recibimos.
    private BufferedReader bufferedReader; //Lo usamos para leer la informacion que el cliente nos manda
    private BufferedWriter bufferedWriter; //Enviar mensajes de manera eficiente

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        try {
            this.socket = serverSocket.accept(); //El programa se detiene aqui hasta que un cliente se conencete con nosostros
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }

    }

    //Funcion para mandar mensajes al cliente:
    public void sendClientMessage(String message){
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

    //Funcion para recibir mensajes del cliente: (Lo debemos ejecutar dentro de un hilo para que no se bloquee el programa esperando mensajes del cliente)
    public void receiveClientMessage(VBox vBox){
        new Thread(() -> {
            while (socket.isConnected()){
                try {
                    String clientMessage = bufferedReader.readLine();
                    ClientController.addLabel(clientMessage,vBox);
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
