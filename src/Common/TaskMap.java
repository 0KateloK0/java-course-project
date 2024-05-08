package Common;

import java.util.ArrayList;
import java.util.HashMap;;

public class TaskMap extends HashMap<Integer, Task> {
    TaskNode root;

    public TaskMap() {
        root = new TaskNode();
    }

    // public Task getNode()

    public class TaskNode {
        public ArrayList<TaskNode> children = new ArrayList<>();
        public Integer taskId;

        public TaskNode(Integer taskId) {
            this.taskId = taskId;
        }

        public TaskNode() {
            // this.taskId = null;
            this(null);
        }
    }
}
