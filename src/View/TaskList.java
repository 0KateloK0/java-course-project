package View;

import java.awt.*;

import Common.Task;
import Common.TaskMap;
import Controller.Controller;

public class TaskList extends Panel {
    public TaskList(Controller controller, TaskMap tasks) {
        for (Task x : tasks.values()) {
            var taskPanel = new TaskPanel(controller, x);
            add(taskPanel);
        }
    }
}
