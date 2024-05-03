package View;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import Common.Task;
import Common.TaskState;
import Common.Commands.ChangeTaskCommand;
import Common.Commands.DeleteTaskCommand;
import Controller.Controller;

public class TaskPanel extends JPanel {
    public static final Integer TASK_PANEL_HEIGHT = 100;
    static final Integer TASK_CHECKBOX_SIZE = TASK_PANEL_HEIGHT / 2;
    private Task task;

    public TaskPanel(Controller controller, Task task) {
        this.task = task;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // setPreferredSize(new Dimension(50, TASK_PANEL_HEIGHT));
        var checkButton = new Checkbox();
        add(checkButton);
        checkButton.setPreferredSize(new Dimension(TASK_CHECKBOX_SIZE, TASK_CHECKBOX_SIZE));
        var descriptionPanel = new Panel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        var nameLabel = new Label(task.name);
        var descriptionLabel = new Label(task.description);
        descriptionPanel.add(nameLabel);
        descriptionPanel.add(descriptionLabel);
        add(descriptionPanel);
        var deadlineLabel = new Label(Task.TASK_DATE_FORMAT.format(task.deadline));
        add(deadlineLabel);
        var deleteButton = new Button("удалить");
        add(deleteButton);
        setVisible(true);

        var self = this;
        checkButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                var newTask = new Task(self.task);
                newTask.state = checkButton.getState() ? TaskState.DONE : TaskState.DUE;
                controller.executeCommand(new ChangeTaskCommand(controller, self.task, newTask));
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.executeCommand(new DeleteTaskCommand(controller, self.task));
            }
        });
    }
}
