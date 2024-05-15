package Common;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;

public class Task implements JSONifiable, JSONString, Cloneable {
    private static Integer lastId = 1;
    public Integer id = 0;
    public String name = "";
    public String description = "";
    public GregorianCalendar deadline;
    public TaskState state = TaskState.DUE;

    public static final DateFormat TASK_DATE_FORMAT = new SimpleDateFormat("y MM dd HH:mm");

    // public JSONObject toJSONObject() {

    // }

    public Task() {
        deadline = new GregorianCalendar();
        id = ++lastId;
    }

    public Task(Task task) {
        id = task.id;
        name = task.name;
        description = task.description;
        deadline = (GregorianCalendar) task.deadline.clone();
        state = task.state;
    }

    /**
     * @warning создает полную копию, включая id. Если нужен новый объект,
     * @warning id надо назначить по новой с помощью метода assignUniqueId
     */
    public Object clone() {
        return new Task(this);
    }

    public void assignUniqueId() {
        id = ++lastId;
    }

    public Task fromJSONObject(JSONObject obj) throws ParseException {
        this.id = obj.getInt("id");
        this.name = obj.getString("name");
        this.description = obj.getString("description");
        var date_string = obj.getString("deadline");
        var cal = new GregorianCalendar();
        cal.setTime(TASK_DATE_FORMAT.parse(date_string));
        this.deadline = cal;
        this.state = obj.getEnum(TaskState.class, "state");
        return this;
    }

    @Override
    public String toJSONString() {
        return new JSONStringer()
                .object()
                .key("id").value(id)
                .key("name").value(name)
                .key("description").value(description)
                .key("deadline").value(TASK_DATE_FORMAT.format(deadline))
                .key("state").value(state)
                .endObject()
                .toString();
    }
}
