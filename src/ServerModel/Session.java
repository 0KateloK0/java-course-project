package ServerModel;

import Common.TaskMap;
import Common.User;

public class Session {
    private User user;
    private TaskMap tasks;

    public synchronized TaskMap getTasks() {
        return tasks;
    }

    public synchronized void setTasks(TaskMap tasks) {
        this.tasks = tasks;
    }

    public synchronized User getUser() {
        return user;
    }

    public synchronized void setUser(User user) {
        this.user = user;
    }
}
