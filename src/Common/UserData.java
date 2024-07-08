package Common;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONObject;
import org.json.JSONStringer;
import Model.Model;

public class UserData implements JSONifiable {
    public TaskMap tasks;
    public int lastTaskId;
    public GregorianCalendar lastChanged;

    @Override
    public UserData fromJSONObject(JSONObject obj) throws ParseException {
        var tasksJSON = obj.getJSONArray("tasks");
        tasks = new TaskMap();
        for (int j = 0; j < tasksJSON.length(); ++j) {
            try {
                var task = new Task().fromJSONObject(tasksJSON.getJSONObject(j));
                tasks.createTask(task);
            } catch (ParseException e) {
                e.printStackTrace();
                // TODO: пометить это задание как задание с ошибкой
            }
        }

        var metadataJSON = obj.getJSONObject("metadata");
        lastTaskId = metadataJSON.getInt("lastTaskId");
        return this;
    }

    @Override
    public String toJSONString() {
        var obj = new JSONStringer()
                .object()
                .key("tasks").array();
        for (var task : tasks.values()) {
            obj.value(task.toJSONObject());
        }
        obj.endArray()
                .key("metadata")
                .object()
                .key("lastChanged").value(Model.DATE_FORMAT.format(new Date()))
                .key("lastTaskId")
                .value(((Task.DefaultIdGenerator) Task.getIdGenerator()).getLastId())
                .endObject()
                .endObject();
        return obj.toString();
    }
}
