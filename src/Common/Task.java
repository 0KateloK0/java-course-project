package Common;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import org.json.JSONObject;

public class Task implements JSONifiable {
    private static Integer lastId = 1;
    public Integer id = 0;
    public String name = "";
    public String description = "";
    public Date deadline;
    public TaskState state = TaskState.DUE;

    public static final DateFormat TASK_DATE_FORMAT = new SimpleDateFormat("y MM dd HH:mm");

    // public JSONObject toJSONObject() {

    // }

    public Task() {
        deadline = new Date();
        id = ++lastId;
    }

    public Task(Task task) {
        id = task.id;
        name = task.name;
        description = task.description;
        deadline = task.deadline;
        state = task.state;
    }

    public Task fromJSONObject(JSONObject obj) throws ParseException {
        this.id = obj.getInt("id");
        this.name = obj.getString("name");
        this.description = obj.getString("description");
        var date_string = obj.getString("deadline");
        this.deadline = TASK_DATE_FORMAT.parse(date_string);
        this.state = obj.getEnum(TaskState.class, "state");
        return this;
    }
}
