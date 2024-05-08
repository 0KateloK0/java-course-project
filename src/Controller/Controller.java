package Controller;

import Model.Model;
import View.View;
import java.util.function.Function;

import javax.swing.SwingUtilities;

import Common.Task;
import Common.Commands.AbstractCommand;
import Common.Commands.CommandHistory;

public class Controller {
    public Model model;
    public View view;
    public CommandHistory commandHistory;

    public Controller() {
        commandHistory = new CommandHistory();

        // view = new View(this, new Function<String, Boolean>() {
        // public Boolean apply(String s) {
        // view.loadMainScreen(); // КОСТЫЛЬ!!!
        // return true;
        // }
        // });

        // КОСТЫЛЬ
        View.setView(this, new Function<String, Boolean>() {
            public Boolean apply(String s) {
                view.loadMainScreen(); // КОСТЫЛЬ!!!
                return true;
            }
        });
        // model = new Model(this, view);
    }

    public void setView(View view) {
        this.view = view;
        this.model = new Model(this, view);
    }

    public void mainLoop() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.promptUser();
            }
        });

    }

    public void executeCommand(AbstractCommand cmd) {
        commandHistory.add(cmd);
        cmd.execute();
    }

    public void addTask(Task task) {
        model.addTask(task);

    }

    public void deleteTask(int index) {
        model.deleteTask(index);
    }

    public void changeTask(Task oldTask, Task newTask) {
        model.changeTask(oldTask.id, newTask);
    }
}
