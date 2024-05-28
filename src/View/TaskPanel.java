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
import Common.Commands.ChangeTaskCommand;
import Common.Commands.DeleteTaskCommand;
import Controller.Controller;
import Model.Model;

public class TaskPanel extends JPanel implements ActionListener {
    public static final Integer TASK_PANEL_HEIGHT = 100;
    static final Integer TASK_CHECKBOX_SIZE = TASK_PANEL_HEIGHT / 2;
    private Task task;
    private final String CHECK_ACTION_COMMAND = "CHECK";
    private final String EDIT_ACTION_COMMAND = "EDIT";
    private final String DELETE_ACTION_COMMAND = "DELETE";
    Controller controller;

    public TaskPanel(Controller controller, Task task) {
        this.task = task;
        this.controller = controller;

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
        checkButton.setActionCommand(CHECK_ACTION_COMMAND);
        checkButton.addActionListener(this);
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
        var deadlineLabel = new JLabel(Model.DATE_FORMAT.format(task.deadline.getTime()));
        add(deadlineLabel, c);

        c.gridx = 4;
        c.weightx = 0.2;
        var editButton = new JButton("РЕДАКТИРОВАТЬ");
        editButton.setActionCommand(EDIT_ACTION_COMMAND);
        editButton.addActionListener(this);
        add(editButton, c);

        c.gridx = 5;
        c.weightx = 0.2;
        var deleteButton = new JButton("УДАЛИТЬ");
        deleteButton.setActionCommand(DELETE_ACTION_COMMAND);
        deleteButton.addActionListener(this);
        add(deleteButton, c);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CHECK_ACTION_COMMAND:
                var newTask = new Task(task);
                newTask.state = Task.State.DONE;
                controller.executeCommand(new ChangeTaskCommand(controller, task, newTask));
                break;
            case EDIT_ACTION_COMMAND:
                controller.openEditManager();
                break;
            case DELETE_ACTION_COMMAND:
                controller.executeCommand(new DeleteTaskCommand(controller, task));
                break;
            default:
                break;
        }
    }
}
