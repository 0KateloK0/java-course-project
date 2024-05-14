package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;

import Common.TaskMap;
import Controller.Controller;

public class TaskManager extends JPanel implements ActionListener {
    private TaskCreationManager taskCreationManager;
    private TaskList taskList;
    private final int CONTROL_PANEL_HEIGHT = 100;
    private final int TASK_CREATION_PANEL_HEIGHT = 100;
    private final String UNDO_ACTION_COMMAND = "UNDO";
    private final String REDO_ACTION_COMMAND = "REDO";
    private final String NEW_ACTION_COMMAND = "NEW";
    Controller controller;

    public TaskManager(Controller controller) {
        this.controller = controller;

        setBorder(new EmptyBorder(100, 100, 100, 100));

        var layout = new BorderLayout();
        setLayout(layout);
        layout.setHgap(20);

        var controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(getWidth(), CONTROL_PANEL_HEIGHT));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        add(controlPanel, BorderLayout.NORTH);

        var creationButton = new JButton("Новое задание");
        creationButton.setActionCommand(NEW_ACTION_COMMAND);
        creationButton.addActionListener(this);
        controlPanel.add(creationButton);

        var undoButton = new JButton("Undo");
        undoButton.setActionCommand(UNDO_ACTION_COMMAND);
        undoButton.addActionListener(this);
        controlPanel.add(undoButton);

        var redoButton = new JButton("Redo");
        redoButton.setActionCommand(REDO_ACTION_COMMAND);
        redoButton.addActionListener(this);
        controlPanel.add(redoButton);

        taskList = new TaskList(controller);
        add(taskList, BorderLayout.CENTER);

        taskCreationManager = new TaskCreationManager(controller);
        taskCreationManager.setVisible(false);
        taskCreationManager.setPreferredSize(new Dimension(getWidth(), TASK_CREATION_PANEL_HEIGHT));
        add(taskCreationManager, BorderLayout.SOUTH);
    }

    public void updateTasks(TaskMap tasks) {
        taskList.setTasks(tasks);
        validate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case UNDO_ACTION_COMMAND:
                controller.undoCommand();
                break;
            case REDO_ACTION_COMMAND:
                controller.redoCommand();
                break;
            case NEW_ACTION_COMMAND:
                if (taskCreationManager.isVisible()) {
                    remove(taskCreationManager);
                } else {
                    add(taskCreationManager, BorderLayout.SOUTH);
                }
                taskCreationManager.setVisible(!taskCreationManager.isVisible());
                break;
            default:
                System.err.println("WTF");
                break;
        }
    }
}
