package GUI;

import Controller.PlayerController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    public static PlayerController playerController;
    private static final int port = 6667;

    private static String playerTurn;

    // Lista de Jogadores Disponíveis
    static List<ClientHandler> clientsList = new ArrayList<>();

    private final HashMap<String, ClientHandler> pp = new HashMap<>();
    private static final  List<String> players = new ArrayList<>();


    // Número de Jogadores Connectados
    static int numPlayers = 0;

    // IP de um Novo Cliente
    static int id = 0;
    private static Socket socket;

    public static void main(String[] args) throws IOException {
        System.out.println("Server active at: " + port);
        ServerSocket serverSockets = new ServerSocket(port);

        Thread servidor = new Thread(() -> {

            while (true) {
                try {
                    socket = serverSockets.accept();

                    DataInputStream inputData = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputData = new DataOutputStream(socket.getOutputStream());
                    ObjectInputStream inputObject = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream outputObject = new ObjectOutputStream(socket.getOutputStream());

                    System.out.println("New Client: " + socket);
                    ClientHandler mtch = new ClientHandler(socket, "Cliente " + id, inputData, outputData, id, inputObject, outputObject);

                    Thread t = new Thread(mtch);

                    clientsList.add(mtch);
                    outputData.writeUTF("playerName" + mtch.code);
                    String idNome = Integer.toString(id);
                    t.setName(idNome);

                    t.start();

                    numPlayers++;
                    id++;
                    System.out.println("Client " + id + " added" + socket.getInetAddress());

                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        servidor.start();
    }

    static class ClientHandler implements Runnable {

        String code;
        String name;
        private String userName;
        final DataInputStream inputData;
        final DataOutputStream outputData;
        Socket socket;
        boolean isloggedin;
        final int id;
        ObjectInputStream inputObject;
        ObjectOutputStream outputObject;
        boolean readyToPlay;
        private boolean finish;

        private ClientHandler(Socket socket, String string,
                              DataInputStream inputData, DataOutputStream outputData, int id, ObjectInputStream inputObject, ObjectOutputStream outputObject) {
            this.socket = socket;
            this.inputData = inputData;
            this.outputData = outputData;
            this.code = string;
            this.id = id;
            this.isloggedin = true;
            this.inputObject = inputObject;
            this.outputObject = outputObject;
            this.name = null;
            this.readyToPlay = false;
            this.finish = false;
        }
        static String received;

        // verifica se os jogadores estão prontos para jogar
        private boolean playersReady(){
            boolean result = true;
            if(Server.numPlayers < 2) result = false;
            else{
                for(ClientHandler c : Server.clientsList){
                    if(!c.readyToPlay) result = false;
                }
            }
            return result;
        }

        // devolve o nome do outro jogador
        private String getOtherPlayerName(String codeJogador){
            String result = "";
            for(ClientHandler c : Server.clientsList){
                if(!c.code.equals(codeJogador)) result = c.name;
            }
            return result;
        }

//        public boolean generateTurn(){
//            boolean result = false;
//            Random r = new Random();
//            int random = r.nextInt();
//            if(random % 2 == 0) result = true;
//            return result;
//        }

        @Override
        public void run() {

            if(Server.numPlayers > 2) {
                try{
                    System.out.println("Room Full..");
                    outputData.writeUTF("#roomfull");
                    this.isloggedin = false;
                    Server.clientsList.remove(this);
                    Server.numPlayers--;
                    this.socket.close();
                    return;
                }
                catch (Exception e) {
                };
            }
            Thread clientT = new Thread(() -> {
                while (true) {

                    try {
                        received = inputData.readUTF();
                        if (received.endsWith("#logout")) {
                            for (ClientHandler client : Server.clientsList) {
                                if (!client.code.equals(code) && client.isloggedin) {
                                    client.outputData.writeUTF("#logout-" + name);
                                }

                            }
                            this.isloggedin = false;
                            Server.clientsList.remove(this);
                            Server.numPlayers--;
                            System.out.println("Player " + this.name + " left the game...");
                            this.socket.close();

                            for(ClientHandler c : Server.clientsList){
                                // avisar o cliente que terá que esperar agora
                                if(Server.numPlayers < 2 && c.readyToPlay){
                                    if(this.finish) c.outputData.writeUTF("#waitGG");
                                    else c.outputData.writeUTF("#waitSurrender");
                                }
                            }

                            break; // while
                        }
                        else if (received.startsWith("#name")){
                            // #nome-nomeJogador
                            String nomeAux = received.split("-")[1];
                            System.out.println(nomeAux + " ready");
                            name = nomeAux;
                            System.out.println("Players " + players);
                            readyToPlay = true;
                            boolean ready = playersReady();
                            System.out.println("READY TO PLAY? " + ready);
//                            boolean vez = this.generateTurn();
                            for (ClientHandler client : Server.clientsList) {
                                if (!client.code.equals(code) && client.isloggedin) {
                                    client.userName = name;
                                    // #nome-nomeJogador-pronto-vez
                                    players.add(name);
                                    System.out.println("Players1 " + players);
                                    String message = "#name-" + name;
                                    if(ready) message += "-" + "READY-";
                                    System.out.println("MESSAGE: " + message);
                                    client.outputData.writeUTF(message);
                                    trySortPlayer();
                                }
                                else{
                                    if(ready){
                                        // #pronto-nomeAdversario-vez
                                        String otherPlayerName = getOtherPlayerName(code);
                                        client.userName = otherPlayerName;
                                        players.add(otherPlayerName);
                                        System.out.println("Players2 " + players);
                                        System.out.println("MESSAGE: " + "#ready-" + otherPlayerName + "-");
                                        client.outputData.writeUTF("#ready-" + otherPlayerName + "-");
                                        trySortPlayer();
                                    }
                                }
                            }
                        }
                        else if(received.startsWith("#PLAY")){
                            String replace = received.replace("#PLAY-", "");
                            int i = replace.indexOf("-");
                            if (i != -1){
                                String name = replace.substring(0, i);
                                String dados = replace.substring(i + 1);
                                for (ClientHandler clientHandler : Server.clientsList) {
                                    if (!name.equals(clientHandler.name)) {
                                        clientHandler.outputData.writeUTF("#PLAY-" + dados);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            clientT.start();

        }

        private void trySortPlayer() throws IOException {
            if (players.size() == 2) {
                String player1 = players.get(0);
                String player2 = players.get(1);

                ClientHandler player1Handler = null;
                ClientHandler player2Handler = null;

                for (ClientHandler client : Server.clientsList) {
                    if (player1.equals(client.name)) {
                        player1Handler = client;
                    }
                    else if (player2.equals(client.name)){
                        player2Handler = client;
                    }
                }
                playerTurn = player1;
                player1Handler.outputData.writeUTF("#PLAYER1");
                player2Handler.outputData.writeUTF("#PLAYER2");
            }
        }
    }
}
