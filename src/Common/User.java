package Common;

import java.text.ParseException;

import org.json.JSONObject;
import org.json.JSONStringer;

public class User implements JSONifiable {
    public String name;
    public Integer id;

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
        return User.idGenerator;
    }

    public static void setIdGenerator(IdGenerator idGenerator) {
        User.idGenerator = idGenerator;
    }

    public User() {
        id = idGenerator.getNewId();
        name = "";
    }

    public User fromJSONObject(JSONObject obj) throws ParseException {
        this.name = obj.getString("name");
        this.id = obj.getInt("id");
        return this;
    }

    @Override
    public String toJSONString() {
        return new JSONStringer()
                .object()
                .key("name").value(name)
                .key("id").value(id)
                .endObject()
                .toString();
    }
}
