import java.util.HashSet;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
// import java.net.http.;

public class Server {
    // private Socket socket = null;
    private ServerSocket server = null;

    private HashSet<String> users = new HashSet<>();

    class RequestProcessingThread extends Thread {
        private Socket socket = null;
        private BufferedReader in = null;
        private DataOutputStream out = null;

        public RequestProcessingThread(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
            } catch (Exception ignore) {
            }

        }

        @Override
        public void run() {
            try {
                var request = new ArrayList<String>();
                String line = "";
                while (!line.equals("Over")) {
                    try {
                        line = in.readLine();
                        request.add(line);
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                    }
                }
                parseRequest(request);

                socket.close();
                in.close();
                out.close();
            } catch (Exception ignored) {
            }
        }

        public void parseRequest(ArrayList<String> request) {
            try {
                System.out.println(request.get(0).contains("GET"));
                if (request.get(0).contains("GET")) {
                    System.out.println(request.get(0).contains("/user/"));
                    if (request.get(0).contains("/user/")) {
                        System.out.println(request.get(0).split("/"));
                        out.writeUTF(
                                (users.contains(request.get(0).split("/")[2]) ? "true" : "false") + "\n");
                        return;
                    }
                } else {

                }

                out.writeUTF("400\n");

            } catch (Exception ignore) {
                ignore.printStackTrace();
            }

        }
    }

    Server(int port) {
        users.add("Artem");
        try {
            server = new ServerSocket(port);
            while (true) {
                var socket = server.accept();
                System.out.println(socket);

                var requestProcessingThread = new RequestProcessingThread(socket);
                requestProcessingThread.start();
            }
        } catch (Exception ignore) {
        }
    }

    public static void main(String[] args) {
        var server = new Server(4444);

    }
}
