package Common;

import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONStringer;

public class CachedData implements JSONifiable {
    public UserMap users;

    @Override
    public String toJSONString() {
        return new JSONStringer()
                .object()
                .key("users").array().endArray()
                .endObject()
                .toString();
    }

    @Override
    public CachedData fromJSONObject(JSONObject obj) throws ParseException {
        users = new UserMap();
        var usersJSON = obj.getJSONArray("users");
        for (int i = 0; i < usersJSON.length(); ++i) {
            try {

                var user = new User().fromJSONObject(usersJSON.getJSONObject(i));
                users.createUser(user);
            } catch (ParseException e) {
                e.printStackTrace(); // TODO: пометить этого юзера как некорректного
            }
        }

        return this;
    }

}
