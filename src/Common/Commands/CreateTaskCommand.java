package Common.Commands;

import Common.Task;
import Controller.Controller;

public class CreateTaskCommand extends AbstractCommand {
    Controller controller;
    Task task;

    public CreateTaskCommand(Controller controller, Task task) {
        this.controller = controller;
        this.task = task;
    }

    public void execute() {
        controller.addTask(this.task);
    }

    public Boolean undo() {
        controller.deleteTask(task.id); // TODO: добавить проверку что задание существует
        return true;
    }
}
