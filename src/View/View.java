package View;

import java.awt.Font;
import java.awt.BorderLayout;
// import java.awt.Frame;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
// import javax.swing.SwingUtilities;
import javax.swing.SwingUtilities;

import java.util.function.Function;

import Common.TaskMap;
import Controller.Controller;

public class View extends JFrame {
    Controller controller;
    private JPanel container;
    private UserPrompt userPrompt;
    private TaskManager taskManager;
    private static String USER_PROMPT_CARD = "user prompt card";
    private static String TAKS_INTERFACE_CARD = "task interface card";
    public static Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    public View(Controller controller, Function<String, Boolean> verifier) {
        // super(new CardLayout());
        this.controller = controller;
        setPreferredSize(new Dimension(1920, 1080));
        setTitle("Todo");
        setVisible(true);

        container = new JPanel(new CardLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        userPrompt = new UserPrompt(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (verifier.apply(userPrompt.getUsername())) {
                    userPrompt.setIsRepeat(true);
                }
            }
        });

        taskManager = new TaskManager(controller);

        // setLayout(new CardLayout());

        container.add(userPrompt, USER_PROMPT_CARD);
        container.add(taskManager, TAKS_INTERFACE_CARD);
        add(container, BorderLayout.CENTER);
        pack();
    }

    public void promptUser() {
        // SwingUtilities.invokeLater(
        // new Runnable() {
        // public void run() {
        var cl = (CardLayout) (container.getLayout());
        cl.show(container, USER_PROMPT_CARD);
        // }
        // });

    }

    public void loadMainScreen() {
        var cl = (CardLayout) (container.getLayout());
        cl.show(container, TAKS_INTERFACE_CARD);
    }

    public void updateTasks(TaskMap tasks) {
        taskManager.updateTasks(tasks);
    }

    public static void setView(Controller controller, Function<String, Boolean> verifier) {
        javax.swing.SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        var view = new View(controller, verifier);
                        controller.setView(view);
                    }
                });
    }

    public static void createAndShowGUI(Controller controller, Function<String, Boolean> verifier) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        var view = new View(controller, verifier);
                        controller.setView(view);
                    }
                });
    }
}
