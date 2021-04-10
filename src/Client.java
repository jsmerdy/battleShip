import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static PrintWriter socketWriter;
    private static BufferedReader socketReader;

    public static void main(String[] args) {
        try {
            System.out.println("client> connecting to 127.0.0.1:5000...");
            Socket socket = new Socket("127.0.0.1", 5000);
            System.out.println("client> success!");

            socketWriter = new PrintWriter(socket.getOutputStream());
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Client().go();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void go() {
        Grid grid = new Grid();
        try {
            while(true) {
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(System.in)));
                String line = bufferedReader.readLine();
                socketWriter.println(line);
                socketWriter.flush();

                try {
                    String retval = socketReader.readLine();
                    System.out.println(retval);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
