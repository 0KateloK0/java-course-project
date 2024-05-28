package View;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import java.util.Enumeration;

import Common.TaskMap;
import Controller.Controller;
import Model.Model;

public class View extends JFrame implements PropertyChangeListener {
    Controller controller;
    Model model;
    private JPanel container;
    private UserPrompt userPrompt;
    private TaskManager taskManager;
    private StatePanel statePanel;
    private static String USER_PROMPT_CARD = "user prompt card";
    private static String TAKS_INTERFACE_CARD = "task interface card";
    public static Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 20);
    private boolean isOnline = false;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        statePanel.setOnline(isOnline);
    }

    public View(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;

        // задает шрифт
        {
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    FontUIResource orig = (FontUIResource) value;
                    Font font = new Font(FONT.getFontName(), orig.getStyle(), FONT.getSize());
                    UIManager.put(key, new FontUIResource(font));
                }
            }
        }

        setPreferredSize(new Dimension(1920, 1080));
        setTitle("Todo");
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // controller.model.serverConnection.close();
                controller.close();
                System.exit(0);
            }
        });

        container = new JPanel(new CardLayout());
        statePanel = new StatePanel(false, null);
        userPrompt = new UserPrompt(controller);

        // taskManager = new TaskManager(controller);

        container.add(userPrompt, USER_PROMPT_CARD);
        // container.add(taskManager, TAKS_INTERFACE_CARD);
        add(statePanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        pack();
    }

    public void promptUser() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                var cl = (CardLayout) (container.getLayout());
                cl.show(container, USER_PROMPT_CARD);
            }
        });

    }

    public void loadMainScreen() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                taskManager = new TaskManager(controller);
                taskManager.updateTasks(model.getTasks());
                container.add(taskManager, TAKS_INTERFACE_CARD);
                var cl = (CardLayout) (container.getLayout());
                cl.show(container, TAKS_INTERFACE_CARD);
            }
        });
    }

    public void updateTasks(TaskMap tasks) {
        taskManager.updateTasks(tasks);
    }

    public void showError(String error) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName());
        System.out.println(evt.getNewValue());
        switch (evt.getPropertyName()) {
            case "state":
                setOnline((boolean) evt.getNewValue());
                break;
            case "tasks":
                updateTasks((TaskMap) evt.getNewValue());
                break;
            default:
                System.err.println("Неправильное или необработанное событие: " + evt.getPropertyName());
        }
    }
}
