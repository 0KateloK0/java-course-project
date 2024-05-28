package Common;

import java.text.ParseException;

import org.json.JSONObject;

public interface JSONifiable {
    // public String toString();

    public String toJSONString();

    // JSONifiable (JSONObject obj);

    // public static JSONifiable fromJSONObject(JSONObject obj) throws
    // ParseException {
    // return Object();
    // }
    public JSONifiable fromJSONObject(JSONObject obj) throws ParseException;
}
