package View;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
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
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));

        var c = new GridBagConstraints();

        var checkButton = new JButton(" ");
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.1;
        add(checkButton, c);

        var descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        var nameLabel = new JLabel(task.name);
        var descriptionLabel = new JLabel(task.description);
        descriptionPanel.add(nameLabel);
        descriptionPanel.add(descriptionLabel);
        c.gridx = 2;
        c.weightx = 0.5;
        add(descriptionPanel, c);

        c.gridx = 3;
        c.weightx = 0.2;
        var deadlineLabel = new JLabel(Task.TASK_DATE_FORMAT.format(task.deadline.getTime()));
        add(deadlineLabel, c);

        c.gridx = 4;
        c.weightx = 0.2;
        var deleteButton = new JButton("УДАЛИТЬ");
        add(deleteButton, c);

        setVisible(true);

        var self = this;
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                var newTask = new Task(self.task);
                newTask.state = TaskState.DONE;
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
