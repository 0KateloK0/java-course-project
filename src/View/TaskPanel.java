package View;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BoxLayout;

import Common.Task;
import Common.TaskState;
import Common.Commands.ChangeTaskCommand;
import Controller.Controller;

public class TaskPanel extends Panel {
    private Task task;

    public TaskPanel(Controller controller, Task task) {
        this.task = task;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        var checkButton = new Checkbox();
        add(checkButton);
        var descriptionPanel = new Panel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        var nameLabel = new Label(task.name);
        var descriptionLabel = new Label(task.description);
        descriptionPanel.add(nameLabel);
        descriptionPanel.add(descriptionLabel);
        add(descriptionPanel);
        var deadlineLabel = new Label(Task.TASK_DATE_FORMAT.format(task.deadline));
        add(deadlineLabel);
        setVisible(true);

        var self = this;
        checkButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                var newTask = new Task(self.task);
                newTask.state = checkButton.getState() ? TaskState.DONE : TaskState.DUE;
                controller.executeCommand(new ChangeTaskCommand(controller.model, self.task, newTask));
            }
        });
    }
}
