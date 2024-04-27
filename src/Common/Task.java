package Common;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import org.json.JSONObject;

public class Task implements JSONifiable {
    public Integer id;
    public String name = "";
    public String description = "";
    public Date deadline;

    public static final DateFormat TASK_DATE_FORMAT = new SimpleDateFormat("y MM dd HH:mm");

    // public JSONObject toJSONObject() {

    // }

    public Task fromJSONObject(JSONObject obj) throws ParseException {
        this.id = obj.getInt("id");
        this.name = obj.getString("name");
        this.description = obj.getString("description");
        var date_string = obj.getString("deadline");
        this.deadline = TASK_DATE_FORMAT.parse(date_string);
        return this;
    }
}
