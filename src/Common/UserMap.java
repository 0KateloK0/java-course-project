package Common;

import java.util.HashMap;

public class UserMap extends HashMap<Integer, User> {
    public boolean contains(String name) {
        return readUser(name) != null;
    }

    public User readUser(String name) {
        for (var user : values()) {
            if (user.name.equals(name))
                return user;
        }
        return null;
    }

    public User readUser(Integer user_id) {
        return get(user_id);
    }

    public void createUser(User user) {
        put(user.id, user);
    }

    public void deleteUser(User user) {
        remove(user.id);
    }

    public void deleteUser(Integer user_id) {
        remove(user_id);
    }

    public void updateUser(User user) {
        put(user.id, user);
    }
}
