package View;

import java.awt.*;

import Common.Task;
import Common.TaskMap;
import Controller.Controller;

public class TaskList extends Panel {
    public TaskMap tasks;
    Controller controller;

    public TaskList(Controller controller) {
        this.controller = controller;
    }

    public void setTasks(TaskMap tasks) {
        this.tasks = tasks;
        removeAll();
        for (Task x : tasks.values()) {
            var taskPanel = new TaskPanel(controller, x);
            add(taskPanel);
        }
    }
}
