package Model.Loader;

import Common.Task;
import Model.User;
import java.util.HashMap;

public interface LoaderInterface {
    public HashMap<Integer, Task> loadTasks();

    public HashMap<Integer, User> loadUsers();
}
