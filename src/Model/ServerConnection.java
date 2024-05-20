package Model;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import Common.Task;

public class ServerConnection implements Closeable {
    Socket socket;
    DataOutputStream out;
    BufferedReader in;

    public ServerConnection() throws IOException, UnknownHostException {
        String loopback = "127.0.0.1";
        String server = loopback;
        socket = new Socket(server, 4444);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void close() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (Exception ignored) {
        }

    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void send(String data) throws IOException {
        System.out.println(data);
        out.writeUTF(data + "\nOver\n");
    }

    public String receive() throws IOException {
        var res = in.readLine();
        return res;
    }

    public void createTask(Task task) throws IOException {
        send("POST /task\n" + task.toJSONString());
    }

    public void deleteTask(int index) throws IOException {
        send("DELETE /task/" + String.valueOf(index));
    }

    public void updateTask(int index, Task newTask) throws IOException {
        send("PUT /task/" + String.valueOf(index));
    }

    public boolean checkUser(String uncheckedUser) throws IOException {
        // TODO: переделать это в аутентификацию
        send("GET /user/" + uncheckedUser);
        return receive().contains("true");
    }

    public String readUserTasks(String user) throws IOException {
        send("GET /user/tasks/" + user);
        return receive();
    }

    public void getUser(String user) throws IOException {
        send("GET /user/" + user);

    }
}
