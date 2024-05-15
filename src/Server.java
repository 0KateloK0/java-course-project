import java.util.HashSet;

import Model.FileLoader;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {
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
                if (request.get(0).contains("GET")) {
                    String path = request.get(0).split(" ")[1];
                    var route = path.split("/"); // всегда 0 элемент является пустой строкой
                    if (route[1].equals("user")) {
                        if (route[2].equals("tasks")) {

                        } else {
                            out.writeUTF(
                                    (users.contains(request.get(0).split("/")[2]) ? "true" : "false") + "\n");
                        }
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

    Server(int port, String cwd) {
        // users.add("Artem");
        var fileLoader = new FileLoader(cwd + "/users.json");

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
        new Server(4444, "../serverDB");
    }
}
