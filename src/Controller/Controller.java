package Controller;

import Model.Model;
import View.View;

import java.io.Closeable;
import java.util.Stack;

import javax.swing.SwingUtilities;

import Common.Task;
import Common.Commands.AbstractCommand;

public class Controller implements Runnable, Closeable {
    public Model model;
    public View view;
    public Stack<AbstractCommand> commandHistory = new Stack<>();
    public Stack<AbstractCommand> undoHistory = new Stack<>();

    public Controller() {
        SwingUtilities.invokeLater(this);
    }

    @Override
    public void run() {
        model = new Model();
        view = new View(this, model);
        model.addPropertyChangeListener(view);
    }

    public void mainLoop() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                view.promptUser();
            }
        });
    }

    public void authenticate(String username) {
        var res = model.authenticate(username);
        if (res != null) {
            view.loadMainScreen();
        } else {
            view.promptUser();
        }
    }

    public void register(String username) {
        var res = model.register(username);
        if (res != null) {
            view.loadMainScreen();
        } else {
            view.promptUser();
        }
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

    public void openEditManager() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'openEditManager'");
    }

    public void close() {
        model.close();
    }
}
