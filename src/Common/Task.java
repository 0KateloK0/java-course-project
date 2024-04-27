package Common;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import org.json.JSONObject;

public class Task {
    public Integer id;
    public String name = "";
    public String description = "";
    public Date deadline;

    public static final DateFormat TASK_DATE_FORMAT = new SimpleDateFormat("y MM dd HH:mm");

    public static Task fromJSONObject(JSONObject obj) throws ParseException {
        var res = new Task();
        res.id = obj.getInt("id");
        res.name = obj.getString("name");
        res.description = obj.getString("description");
        var date_string = obj.getString("deadline");
        res.deadline = TASK_DATE_FORMAT.parse(date_string);
        return res;
    }
}
