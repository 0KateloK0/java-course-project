package View;

import java.awt.Font;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import java.util.function.Function;

import Common.TaskMap;
import Controller.Controller;

public class View extends JFrame {
    Controller controller;
    private UserPrompt userPrompt;
    private TaskManager taskManager;
    private static String USER_PROMPT_CARD = "user prompt card";
    private static String TAKS_INTERFACE_CARD = "task interface card";
    public static Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    View(Controller controller, Function<String, Boolean> verifier) {
        this.controller = controller;
        setSize(1920, 1080);
        setTitle("Todo");
        setVisible(true);

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

        setLayout(new CardLayout());

        add(userPrompt, USER_PROMPT_CARD);
        add(taskManager, TAKS_INTERFACE_CARD);
    }

    public void promptUser() {
        var cl = (CardLayout) (getLayout());
        cl.show(this, USER_PROMPT_CARD);
    }

    public void loadMainScreen() {
        var cl = (CardLayout) (getLayout());
        cl.show(this, TAKS_INTERFACE_CARD);
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
}
