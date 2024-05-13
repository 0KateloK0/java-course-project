package Model.NetModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import org.json.JSONString;

import Common.Task;

// import javax.swing.SwingWorker;

public class ServerConnection {
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

    public void send(String data) throws IOException {
        System.out.println(data);
        // var out = new DataOutputStream(sock.getOutputStream());
        out.writeUTF(data);
        // out.close();
    }

    public String receive() throws IOException {
        // var in = new DataInputStream(
        // new BufferedInputStream(sock.getInputStream()));
        var res = in.readLine();
        System.out.println(res);
        // in.close();
        return res;
    }

    public void createNewTask(Task task) throws IOException {
        send("POST /task\n" + task.toJSONString() + "\nOver\n");
    }

    public boolean checkUser(String uncheckedUser) throws IOException {
        send("GET /user/" + uncheckedUser + "\nOver\n");
        return receive().contains("true");
    }

    public String getUserInfo(String user) throws IOException {
        send("GET /user_info/" + user + "\nOver\n");
        return receive();
    }
}
