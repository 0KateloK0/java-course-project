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
        // tasks = new ArrayList<Task>();
        for (int i = 0; i < tasksJSON.length(); ++i) {
            Task task = Task.fromJSONObject(tasksJSON.getJSONObject(i));
            // tasks.add(task);
            tasks.put(task.id, task);
        }
    }
}
