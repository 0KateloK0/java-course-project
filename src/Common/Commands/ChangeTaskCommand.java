package Common.Commands;

import Common.Task;
import Controller.Controller;

public class ChangeTaskCommand extends AbstractCommand {
    Controller controller;
    Task oldTask;
    Task newTask;

    public ChangeTaskCommand(Controller controller, Task oldTask, Task newTask) {
        this.controller = controller;
        this.oldTask = oldTask;
        this.newTask = newTask;
    }

    public void execute() {
        this.controller.changeTask(oldTask, newTask);
    }

    public Boolean undo() {
        this.controller.changeTask(newTask, oldTask); // TODO: добавить проверку тчо задание существует
        return true;
    }
}
