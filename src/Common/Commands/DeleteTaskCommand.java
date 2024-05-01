package Common.Commands;

import Common.Task;
import Controller.Controller;

public class DeleteTaskCommand extends AbstractCommand {
    Controller controller;
    Task task;

    public DeleteTaskCommand(Controller controller, Task task) {
        this.controller = controller;
        this.task = task;
    }

    public void execute() {
        controller.deleteTask(task.id);
    }

    public Boolean undo() {
        controller.addTask(task); // TODO: добавить проверку что задание не существует
        return true;
    }
}
