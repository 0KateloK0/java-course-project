package ServerModel;

import java.util.GregorianCalendar;

import Common.Task;
import Common.TaskMap;
import Common.User;
import Common.UserData;

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

    public UserData getUserData() {
        var res = new UserData();
        res.tasks = tasks;
        res.lastChanged = new GregorianCalendar();
        res.lastTaskId = ((Task.DefaultIdGenerator) Task.getIdGenerator()).getLastId();
        return res;
    }
}
