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

public class TaskManager extends JPanel {
    private TaskCreationManager taskCreationManager;
    private TaskList taskList;
    private final int CONTROL_PANEL_HEIGHT = 100;
    private final int TASK_CREATION_PANEL_HEIGHT = 100;

    public TaskManager(Controller controller) {
        // setSize(getPreferredSize());
        var layout = new BorderLayout();
        setLayout(layout);
        layout.setHgap(20);

        setBorder(new EmptyBorder(100, 100, 100, 100));

        var controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        add(controlPanel, BorderLayout.NORTH);

        var creationButton = new JButton("Новое задание");
        controlPanel.add(creationButton);
        var self = this;
        creationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (taskCreationManager.isVisible()) {
                    remove(taskCreationManager);
                } else {
                    add(taskCreationManager, BorderLayout.SOUTH);
                }
                taskCreationManager.setVisible(!taskCreationManager.isVisible());

                self.validate();
                self.repaint();
            }
        });

        var undoButton = new JButton("Undo");
        var redoButton = new JButton("Redo");

        controlPanel.add(undoButton);
        controlPanel.add(redoButton);
        controlPanel.setPreferredSize(new Dimension(getWidth(), CONTROL_PANEL_HEIGHT));

        taskList = new TaskList(controller);
        add(taskList, BorderLayout.CENTER);

        taskCreationManager = new TaskCreationManager(controller);
        add(taskCreationManager, BorderLayout.SOUTH);
        taskCreationManager.setVisible(false);
        taskCreationManager.setPreferredSize(new Dimension(getWidth(), TASK_CREATION_PANEL_HEIGHT));
    }

    public void updateTasks(TaskMap tasks) {
        taskList.setTasks(tasks);
        validate();
        repaint();
    }
}
