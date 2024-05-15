package Model;

import org.json.*;

import java.text.ParseException;
import java.util.HashMap;
import Common.Task;
import Common.TaskMap;
import Common.User;
// import Common.TaskMap.TaskNode;

public class Parser {
    // HashMap<Integer, Task> tasks;
    public TaskMap tasks;
    public HashMap<Integer, User> users;

    public void parse(String data) throws ParseException {
        var obj = new JSONObject(data);

        var tasksJSON = obj.getJSONArray("tasks");
        // this.tasks = new HashMap<>();
        this.tasks = new TaskMap();
        for (int i = 0; i < tasksJSON.length(); ++i) {
            Task task = new Task().fromJSONObject(tasksJSON.getJSONObject(i));

            tasks.new TaskNode();
            tasks.put(task.id, task);
        }

        var usersJSON = obj.getJSONArray("users");
        this.users = new HashMap<>();
        for (int i = 0; i < usersJSON.length(); ++i) {
            var user = new User().fromJSONObject(usersJSON.getJSONObject(i));
            users.put(user.id, user);
        }
    }
}
