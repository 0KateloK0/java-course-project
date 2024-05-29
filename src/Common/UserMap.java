package Common;

import java.util.HashMap;

public class UserMap extends HashMap<Integer, User> {
    public boolean contains(String name) {
        return findByName(name) != null;
    }

    public User findByName(String name) {
        for (var user : values()) {
            if (user.name.equals(name))
                return user;
        }
        return null;
    }
}
