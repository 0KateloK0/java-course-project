package View;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import java.util.Enumeration;

import Common.TaskMap;
import Controller.Controller;

public class View extends JFrame implements WindowListener {
    Controller controller;
    private JPanel container;
    private UserPrompt userPrompt;
    private TaskManager taskManager;
    private static String USER_PROMPT_CARD = "user prompt card";
    private static String TAKS_INTERFACE_CARD = "task interface card";
    public static Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    public View(Controller controller) {
        this.controller = controller;

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

        container = new JPanel(new CardLayout());

        addWindowListener(this);

        userPrompt = new UserPrompt(controller);

        // taskManager = new TaskManager(controller);

        container.add(userPrompt, USER_PROMPT_CARD);
        // container.add(taskManager, TAKS_INTERFACE_CARD);
        add(container, BorderLayout.CENTER);
        pack();
    }

    public void promptUser() {
        var cl = (CardLayout) (container.getLayout());
        cl.show(container, USER_PROMPT_CARD);
    }

    public void loadMainScreen() {
        taskManager = new TaskManager(controller);
        container.add(taskManager, TAKS_INTERFACE_CARD);
        var cl = (CardLayout) (container.getLayout());
        cl.show(container, TAKS_INTERFACE_CARD);
    }

    public void updateTasks(TaskMap tasks) {
        taskManager.updateTasks(tasks);
    }

    public static void createAndShowGUI(Controller controller) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        var view = new View(controller);
                        controller.setView(view);
                        // view.updateTasks(controller.model.getTasks());
                    }
                });
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowActivated'");
    }

    @Override
    public void windowClosed(WindowEvent e) {
        controller.model.serverConnection.close();
        System.exit(0);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        controller.model.serverConnection.close();
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowDeactivated'");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowDeiconified'");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowIconified'");
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method
        // 'windowOpened'");
    }
}
