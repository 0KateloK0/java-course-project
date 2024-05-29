package ServerModel;

import Common.User;

public class Session {
    private User user;

    public synchronized User getUser() {
        return user;
    }

    public synchronized void setUser(User user) {
        this.user = user;
    }
}
