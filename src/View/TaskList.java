package View;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Common.Task;
import Common.TaskMap;
import Controller.Controller;

public class TaskList extends JPanel {
    public TaskMap tasks;
    Controller controller;
    GridBagConstraints c;
    JScrollPane scroll;
    JPanel taskList;

    public TaskList(Controller controller) {
        this.controller = controller;
        c = new GridBagConstraints();
        setLayout(new BorderLayout());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                taskList = new JPanel();
                taskList.setLayout(new GridBagLayout());
                scroll = new JScrollPane(taskList);
                add(scroll, BorderLayout.CENTER);
            }
        });
    }

    public void setTasks(TaskMap tasks) {
        this.tasks = tasks;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                taskList.removeAll();
                for (Task x : tasks.values()) {
                    var taskPanel = new TaskPanel(controller, x);
                    c.gridy = GridBagConstraints.RELATIVE;
                    c.gridx = 0;
                    c.insets = new Insets(20, 0, 20, 0);
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.weightx = 1.0;
                    c.weighty = 0;
                    c.anchor = GridBagConstraints.PAGE_START;

                    taskList.add(taskPanel, c);
                }

                c.weighty = 1;
                taskList.add(new JLabel(" "), c); // костыль нужный чтобы задания отображались в начале контейнера

                validate();
                repaint();
            }
        });
    }
}
