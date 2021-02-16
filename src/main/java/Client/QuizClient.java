package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * runs a client that connects to a server
 */
public class QuizClient {
    private final String HOST;
    private final int PORT;
    final String DELIMITER = "~";
    boolean end;    //loop condition

    /**
     * class constructor that starts a connection with a server given its address and port number
     * @param address   IP address of the server you want to connect to
     * @param port  port number the server is listening on
     */
    public QuizClient(String address, String port) {
        end = false;
        HOST = address;
        PORT = Integer.parseInt(port);
        try (
                Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner in = new Scanner(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            runQuiz(out, in, stdIn);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to start the quiz and start communicating with the server
     *
     * @param out   PrintWriter object to send data to the server
     * @param in    Scanner object to read data from the server
     * @param stdIn BufferedReader object to read data from the user
     * @throws IOException
     */
    public void runQuiz(PrintWriter out, Scanner in, BufferedReader stdIn) throws IOException {
        in.useDelimiter(DELIMITER);
        String code;

        while (!end) {
            code = in.next();
            if (code.equals("MSG")) {
                String message = in.next();
                System.out.println(message);
                if (message.contains("bye"))
                    end = true;
            }
            else if (code.equals("NAME")) {
                System.out.println(in.next());
                out.write(stdIn.readLine());
                out.write(DELIMITER);
            }
            else if (code.equals("PLAYAGAIN")) {
                System.out.println(in.next());
                String input = stdIn.readLine();
                out.write(input);
                out.write(DELIMITER);
                if (input.equals("N") || (input.equals("n"))) {
                    end = true;
                }
            }
            else if (code.equals("QUESTION")) {
                System.out.println(in.next());
                out.write(stdIn.readLine());
                out.write(DELIMITER);
            }
            out.flush();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter the server address");
            String serverAddress = in.nextLine();
            System.out.println("Please enter the port number");
            String portNumber = in.nextLine();
            new QuizClient(serverAddress, portNumber);
        }
        else {
            new QuizClient(args[0], args[1]);
        }
    }
}