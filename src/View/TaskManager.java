package View;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BoxLayout;

import Common.TaskMap;
import Controller.Controller;

public class TaskManager extends Panel {
    private Button creationButton;
    private TaskCreationManager taskCreationManager;
    private TaskList taskList;

    public TaskManager(Controller controller, TaskMap tasks) {
        taskCreationManager = new TaskCreationManager(controller);
        taskCreationManager.setVisible(false);

        taskList = new TaskList(controller, new TaskMap());
        add(taskList);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        creationButton = new Button("Новое задание");
        add(creationButton);
        creationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskCreationManager.setVisible(true);
            }
        });
    }
}
