package View;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Function;

import Controller.Controller;

public class View extends Frame {
    Controller controller;
    private UserPrompt userPrompt;
    private TaskManager taskManager;
    private static String USER_PROMPT_CARD = "user prompt card";
    private static String TAKS_INTERFACE_CARD = "task interface card";
    public static Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 20);

    public View(Controller controller, Function<String, Boolean> verifier) {
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
}
