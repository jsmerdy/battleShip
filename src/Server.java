import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        Grid grid = new Grid();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true) {
                System.out.println("server> waiting for client to connect..");
                Socket client = serverSocket.accept();
                String clientConnected = String.format("server> connected to client Socket[addr=/127.0.0.1,port=%d,localport=5000]", client.getPort());
                System.out.println(clientConnected);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter printWriter = new PrintWriter(client.getOutputStream());

                System.out.println("server> waiting for client to send data..");
                String line;

                while((line = bufferedReader.readLine()) != null) {
                    System.out.println("server> received: " + line);

                    try {
                        line = line.trim();
                        String[] inputArray = line.split(",");
                        int x = Integer.parseInt(inputArray[0]);
                        int y = Integer.parseInt(inputArray[1]);

                        int value = grid.getValue(x,y);
                        System.out.println(">server sent response: " + value);
                        printWriter.println(value);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } finally {
                        printWriter.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
