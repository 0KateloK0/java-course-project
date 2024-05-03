package View;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;

import Common.Task;
import Common.TaskMap;
import Controller.Controller;

public class TaskList extends Panel {
    public TaskMap tasks;
    Controller controller;
    // List taskList;
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
                    System.out.println(x.id);
                }
                setVisible(true);
                System.out.println("Я здесь был");

                c.weighty = 1;
                taskList.add(new Label(" "), c); // костыль нужный чтобы задания отображались в начале контейнера

                // invalidate();
                validate();
                repaint();
            }
        });
    }
}
