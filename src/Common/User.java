package Common;

import java.text.ParseException;

import org.json.JSONObject;

public class User implements JSONifiable {
    public String name;
    public Integer id;
    private static int lastId = 0;

    public User() {
        id = ++lastId;
        name = "";
    }

    public User fromJSONObject(JSONObject obj) throws ParseException {
        this.name = obj.getString("name");
        this.id = obj.getInt("id");
        return this;
    }
}
