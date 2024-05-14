package Controller;

import Model.Model;
import View.View;

import java.util.Stack;

import javax.swing.SwingUtilities;

import Common.Task;
import Common.Commands.AbstractCommand;

public class Controller {
    public Model model;
    public View view;
    public Stack<AbstractCommand> commandHistory;
    public Stack<AbstractCommand> undoHistory;

    public Controller() {
        commandHistory = new Stack<>();
        undoHistory = new Stack<>();
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
        if (!undoHistory.empty()) {
            undoHistory.clear();
        }
        cmd.execute();
    }

    public void undoCommand() {
        if (!commandHistory.isEmpty()) {
            var cmd = commandHistory.pop();
            cmd.undo();
            undoHistory.add(cmd);
        }
    }

    public void redoCommand() {
        if (!undoHistory.empty()) {
            executeCommand(undoHistory.pop());
        }
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
