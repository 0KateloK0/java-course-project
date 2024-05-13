package Controller;

import Model.Model;
import View.View;

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

        // КОСТЫЛЬ, устанавливает view для контроллера
        View.createAndShowGUI(this);
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

    public void verify(String username) {
        model.verifyUser(username);
        // if (model.verifyUser(username)) {
        // view.loadMainScreen();
        // } else {
        // view.promptUser();
        // }
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
