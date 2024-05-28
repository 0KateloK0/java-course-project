package Common;

import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONStringer;

public class CachedData implements JSONifiable {
    public UserMap userMap;

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
        var userMap = new UserMap();
        var usersJSON = obj.getJSONArray("users");
        for (int i = 0; i < usersJSON.length(); ++i) {
            try {

                var user = new User().fromJSONObject(usersJSON.getJSONObject(i));
                userMap.put(user.id, user);
            } catch (ParseException e) {
                e.printStackTrace(); // TODO: пометить этого юзера как некорректного
            }
        }

        this.userMap = userMap;
        return this;
    }

}
