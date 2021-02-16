package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizServer {
    static LeaderBoard obj = new LeaderBoard(new ArrayList<String>(), new ArrayList<Integer>());

    public static void main(String[] args) {
        System.out.println("Starting up the Quiz Server - looking for an open port");
        int port = 7777;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Quiz server listening on port: " + port);
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                Scanner in = new Scanner(clientSocket.getInputStream());
                new Thread(new QuizServerRunnable(obj, in, out)).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
