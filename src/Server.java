import Common.FileManager;
import Common.User;
import Common.UserMap;
import ServerModel.Session;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {
    private ServerSocket server = null;

    private UserMap users;

    class CommunicationThread extends Thread {
        private Socket socket = null;
        private BufferedReader in = null;
        private DataOutputStream out = null;
        // public User activeUser = null;
        public Session session = null;

        public synchronized void write(String response) {
            try {
                out.writeUTF(response);
            } catch (IOException e) {
                e.printStackTrace(); // TODO: обработать правильно
            }
        }

        public CommunicationThread(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace(); // это исключение не должно случаться
            }
            session = new Session();
        }

        @Override
        public void run() {
            try {
                var request = new ArrayList<String>();
                String line = "";
                while (!line.equals("End")) {
                    try {
                        line = in.readLine();
                        if (line.equals("Over")) {
                            var requestProcessingThread = new RequestProcessingThread(this, request);
                            requestProcessingThread.start();
                            request = new ArrayList<>();
                        } else {
                            request.add(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        out.writeUTF("400\n");
                    }
                }
                socket.close();
                in.close();
                out.close();
            } catch (Exception ignored) {
            }
        }

    }

    class RequestProcessingThread extends Thread {
        private CommunicationThread communication;
        private ArrayList<String> request;

        RequestProcessingThread(CommunicationThread communication, ArrayList<String> request) {
            this.communication = communication;
            this.request = request;
        }

        @Override
        public void run() {
            var fileManager = new FileManager();
            if (request.get(0).contains("GET")) {
                String path = request.get(0).split(" ")[1];
                var route = path.split("/"); // всегда 0 элемент является пустой строкой
                if (route[1].equals("user")) {
                    if (route[2].equals("tasks")) {
                        var userData = fileManager.loadUser(communication.session.getUser());
                        communication.write(userData.toJSONString() + "\n");
                    } else {
                        User user = users.findByName(request.get(0).split("/")[2]);
                        if (user != null) {
                            communication.session.setUser(user);
                        }
                        communication.write(
                                (user != null ? user.toJSONString() : "404") + "\n");
                    }
                    return;
                }
            } else if (request.get(0).contains("POST")) {

            }

            communication.write("400\n");
        }

    }

    Server(int port, String cwd) {
        var fileManager = new FileManager();
        var cacheData = fileManager.loadCacheFile(new File(cwd + "/cache.json"));
        users = cacheData.userMap;

        try {
            server = new ServerSocket(port);
            while (true) {
                var socket = server.accept();
                var CommunicationThread = new CommunicationThread(socket);
                CommunicationThread.start();
            }
        } catch (Exception ignore) {
        }
    }

    public static void main(String[] args) {
        new Server(4444, "./serverDB");
    }
}
