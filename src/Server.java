import Common.DebugInfo;
import Common.FileManager;
import Common.Task;
import Common.User;
import Common.UserData;
import Common.UserMap;
import ServerModel.Session;

import java.net.*;
import java.text.ParseException;
import java.io.*;
import java.util.ArrayList;

import org.json.JSONObject;

public class Server {
    private ServerSocket server = null;
    private FileManager fileManager;
    private UserMap users;

    class CommunicationThread extends Thread {
        private Socket socket = null;
        private BufferedReader in = null;
        private BufferedWriter out = null;
        public Session session = null;

        public synchronized void send(String response) {
            try {
                out.write(response);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace(); // TODO: обработать правильно
            }
        }

        public CommunicationThread(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
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
                while (true) {
                    try {
                        line = in.readLine();
                        DebugInfo.print(line);
                        DebugInfo.print(line.length());
                        DebugInfo.print(line.equals("End"));
                        DebugInfo.print((int) line.charAt(line.length() - 1));
                        if (line == null || line.equals("End"))
                            break;
                        if (line.equals("Over")) {
                            var requestProcessingThread = new RequestProcessingThread(this, request);
                            requestProcessingThread.start();
                            request = new ArrayList<>();
                        } else {
                            request.add(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        send("400\n");
                    }
                }
                in.close();
                out.close();
                socket.close();
                fileManager.saveUserFile(session.getUser(), session.getUserData());
            } catch (Exception e) {
                e.printStackTrace();
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
            var splittedRequest = request.get(0).split(" ");
            String method = splittedRequest[0];
            String path = splittedRequest[1];
            var route = path.split("/"); // всегда 0 элемент является пустой строкой
            try {
                switch (method) {
                    case "GET":
                        if (route[1].equals("user")) {
                            if (route[2].equals("tasks")) {
                                var userData = fileManager.loadUser(communication.session.getUser());
                                communication.session.setTasks(userData.tasks);
                                communication.send(userData.toJSONString() + "\n");
                            } else {
                                User user = users.readUser(request.get(0).split("/")[2]);
                                if (user != null) {
                                    communication.session.setUser(user);
                                }
                                communication.send(
                                        (user != null ? user.toJSONString() : "404") + "\n");
                            }
                            break;
                        }
                        break;
                    case "POST":
                        if (route[1].equals("syncronise")) {
                            var userData = new UserData().fromJSONObject(new JSONObject(request.get(1)));
                            if (communication.session.getUser() == null)
                                break;
                            fileManager.saveUserFile(communication.session.getUser(), userData);
                        } else if (route[1].equals("tasks")) {
                            var task = new Task().fromJSONObject(new JSONObject(request.get(1)));
                            communication.session.getTasks().put(task.id, task);
                        } else if (route[1].equals("user")) {
                            var username = request.get(1);
                            // users.put(null, null);
                            var user = new User();
                            user.name = username;
                            users.createUser(user);
                        }
                        break;
                    case "DELETE":
                        if (route[1].equals("tasks")) {
                            var task_id = Integer.valueOf(request.get(1));
                            communication.session.getTasks().remove(task_id);
                        }
                        break;
                    case "UPDATE":
                        if (route[1].equals("tasks")) {
                            var task = new Task().fromJSONObject(new JSONObject(request.get(1)));
                            communication.session.getTasks().put(task.id, task);
                        }
                    default:
                        communication.send("404\n");
                }
            } catch (ParseException | NumberFormatException e) {
                e.printStackTrace();
                communication.send("400\n");
            }
        }
    }

    Server(int port, String cwd) {
        var fileManager = new FileManager("./serverDB/");
        this.fileManager = fileManager;
        var cacheData = fileManager.loadCacheFile(new File(cwd + "/cache.json"));
        users = cacheData.users;

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
