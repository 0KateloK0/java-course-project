package Model;

import java.text.ParseException;

import org.json.JSONObject;

import Common.JSONifiable;

public class User implements JSONifiable {
    public String name;

    public User fromJSONObject(JSONObject obj) throws ParseException {
        this.name = obj.getString("name");
        return this;
    }
}
