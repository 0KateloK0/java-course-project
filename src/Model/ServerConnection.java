package Model;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.ParseException;

import org.json.JSONObject;

import Common.Task;
import Common.User;
import Common.UserData;

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
            out.writeUTF("\nEnd\n");
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

    public User authenticate(String uncheckedUser) throws IOException {
        send("GET /user/" + uncheckedUser);
        var response = receive().substring(1); // странный символ с ascii номером 0 в начале строки
        if (response.contains("404")) {
            return null;
        }
        try {
            return new User().fromJSONObject(new JSONObject(response.stripLeading()));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserData readUserTasks(User user) throws IOException {
        send("GET /user/tasks/" + user.name);
        var response = receive().substring(2); // странный символ с ascii номером 0 в начале строки
        System.out.println(response);
        try {
            return new UserData().fromJSONObject(new JSONObject(response));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createUser(String unregisteredUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }
}
