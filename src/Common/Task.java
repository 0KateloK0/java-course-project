package Common;

import java.util.GregorianCalendar;
import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;

import Model.Model;

public class Task implements JSONifiable, JSONString, Cloneable {
    public Integer id = 0;
    public String name = "";
    public String description = "";
    public GregorianCalendar deadline;
    public TaskState state = TaskState.DUE;

    public static interface IdGenerator {
        public int getNewId();
    }

    public static class DefaultIdGenerator implements IdGenerator {
        private int lastId = 1;

        public int getLastId() {
            return lastId;
        }

        public void setLastId(int lastId) {
            this.lastId = lastId;
        }

        @Override
        public int getNewId() {
            return ++lastId;
        }
    }

    private static IdGenerator idGenerator = new DefaultIdGenerator();

    public static IdGenerator getIdGenerator() {
        return Task.idGenerator;
    }

    public static void setIdGenerator(IdGenerator idGenerator) {
        Task.idGenerator = idGenerator;
    }

    public Task() {
        deadline = new GregorianCalendar();
        assignUniqueId();
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
        id = idGenerator.getNewId();
    }

    public Task fromJSONObject(JSONObject obj) throws ParseException {
        this.id = obj.getInt("id");
        this.name = obj.getString("name");
        this.description = obj.getString("description");
        var date_string = obj.getString("deadline");
        var cal = new GregorianCalendar();
        cal.setTime(Model.DATE_FORMAT.parse(date_string));
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
                .key("deadline").value(Model.DATE_FORMAT.format(deadline.getTime()))
                .key("state").value(state)
                .endObject()
                .toString();
    }

    public JSONObject toJSONObject() {
        var obj = new JSONObject(this, "id", "name", "description", "state");
        obj.put("deadline", Model.DATE_FORMAT.format(deadline.getTime()));
        return obj;
    }
}
