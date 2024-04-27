package Model.Loader;

import org.json.*;

import java.text.ParseException;
import java.util.HashMap;
import Common.Task;

public class Parser {
    HashMap<Integer, Task> tasks;

    public void parse(String data) throws ParseException {
        var obj = new JSONObject(data);
        var tasksJSON = obj.getJSONArray("tasks");
        this.tasks = new HashMap<>();
        for (int i = 0; i < tasksJSON.length(); ++i) {
            Task task = new Task().fromJSONObject(tasksJSON.getJSONObject(i));
            tasks.put(task.id, task);
        }
    }
}
