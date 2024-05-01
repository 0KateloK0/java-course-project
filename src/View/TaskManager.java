package View;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BoxLayout;

import Controller.Controller;

public class TaskManager extends Panel {
    private TaskCreationManager taskCreationManager;
    private TaskList taskList;
    private final int CONTROL_PANEL_HEIGHT = 100;

    public TaskManager(Controller controller) {
        taskCreationManager = new TaskCreationManager(controller);
        taskCreationManager.setVisible(false);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var controlPanel = new Panel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        add(controlPanel);

        var creationButton = new Button("Новое задание");
        controlPanel.add(creationButton);
        creationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskCreationManager.setVisible(true);
            }
        });

        var undoButton = new Button("Undo");
        var redoButton = new Button("Redo");

        controlPanel.add(undoButton);
        controlPanel.add(redoButton);
        controlPanel.setSize(getWidth(), CONTROL_PANEL_HEIGHT);

        taskList = new TaskList(controller);
        add(taskList);
    }
}
