package Common;

// import java.util.ArrayList;
import java.util.HashMap;

public class TaskMap extends HashMap<Integer, Task> {
    // TaskNode root;

    public TaskMap() {
        // root = new TaskNode();
    }

    public void createTask(Task task) {
        put(task.id, task);
    }

    public void deleteTask(Task task) {
        remove(task.id);
    }

    public void deleteTask(Integer task_id) {
        remove(task_id);
    }

    public void updateTask(Task task) {
        put(task.id, task);
    }

    public Task readTask(Integer task_id) {
        return get(task_id);
    }

    // public Task getNode()

    // public class TaskNode {
    // public ArrayList<TaskNode> children = new ArrayList<>();
    // public Integer taskId;

    // public TaskNode(Integer taskId) {
    // this.taskId = taskId;
    // }

    // public TaskNode() {
    // // this.taskId = null;
    // this(null);
    // }
    // }
}
