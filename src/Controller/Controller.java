package Controller;

import Model.Model;
import View.View;
import java.util.function.Function;

import Common.Commands.AbstractCommand;
import Common.Commands.CommandHistory;

public class Controller {
    public Model model;
    public View view;
    public CommandHistory commandHistory;

    public Controller() {
        model = new Model();
        view = new View(this, new Function<String, Boolean>() {
            public Boolean apply(String s) {
                view.loadMainScreen(); // КОСТЫЛЬ!!!
                return true;
            }
        }, model.getTasks());
    }

    public void mainLoop() {
        view.promptUser();
    }

    public void executeCommand(AbstractCommand cmd) {
        commandHistory.add(cmd);
        cmd.execute();
    }
}
