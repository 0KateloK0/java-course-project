package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // setPreferredSize(new Dimension(50, TASK_PANEL_HEIGHT));
        var checkButton = new JCheckBox();
        add(checkButton);
        checkButton.setPreferredSize(new Dimension(TASK_CHECKBOX_SIZE, TASK_CHECKBOX_SIZE));
        var descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS));
        var nameLabel = new JLabel(task.name);
        var descriptionLabel = new JLabel(task.description);
        descriptionPanel.add(nameLabel);
        descriptionPanel.add(descriptionLabel);
        add(descriptionPanel);
        var deadlineLabel = new JLabel(Task.TASK_DATE_FORMAT.format(task.deadline));
        add(deadlineLabel);
        var deleteButton = new JButton("удалить");
        add(deleteButton);
        setVisible(true);

        var self = this;
        checkButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                var newTask = new Task(self.task);
                newTask.state = e.getStateChange() == ItemEvent.DESELECTED ? TaskState.DONE : TaskState.DUE;
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
