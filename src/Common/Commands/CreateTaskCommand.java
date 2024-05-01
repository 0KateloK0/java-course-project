package Common.Commands;

import Model.Model;
import Common.Task;

public class CreateTaskCommand extends AbstractCommand {
    Model model;
    Task task;

    public CreateTaskCommand(Model model, Task task) {
        this.model = model;
        this.task = task;
    }

    public void execute() {
        model.addTask(this.task);
    }
}
