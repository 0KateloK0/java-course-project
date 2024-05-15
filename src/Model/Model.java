package Model;

import Common.Task;
import Common.TaskMap;
import Common.User;
import Controller.Controller;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import View.View;

public class Model implements Closeable {
    TaskMap tasks;
    Controller controller;
    View view;
    ServerConnection serverConnection;
    User activeUser;

    private abstract class State {
        public abstract void addTask(Task task);

        public abstract void deleteTask(int index);

        public abstract void changeTask(int index, Task newTask);

        public abstract void verifyUser(String uncheckedUser);
    }

    private class OnlineState extends State {
        @Override
        public void addTask(Task task) {
            try {
                serverConnection.createTask(task);
                tasks.put(task.id, task);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void deleteTask(int index) {
            try {
                serverConnection.deleteTask(index);
                tasks.remove(index);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void changeTask(int index, Task newTask) {
            try {
                serverConnection.updateTask(index, newTask);
                tasks.put(index, newTask);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void verifyUser(String uncheckedUser) {
            (new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    try {
                        return serverConnection.checkUser(uncheckedUser);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return true;
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            view.loadMainScreen();
                        } else {
                            view.promptUser();
                        }
                    } catch (Exception ignore) {
                        view.promptUser();
                    }
                }
            }).execute();
        }
    }

    private class OfflineState extends State {
        @Override
        public void addTask(Task task) {
            tasks.put(task.id, task);
        }

        @Override
        public void deleteTask(int index) {
            tasks.remove(index);
        }

        @Override
        public void changeTask(int index, Task newTask) {
            tasks.put(index, newTask);
        }

        @Override
        public void verifyUser(String uncheckedUser) {
            // TODO: check cache
        }

    }

    State state;

    public Model(Controller controller, View view) {
        tasks = new TaskMap();

        this.controller = controller;
        this.view = view;
        class ServerConnectionThread extends SwingWorker<ServerConnection, Void> {
            @Override
            protected ServerConnection doInBackground() {
                try {
                    return new ServerConnection();
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            public void done() {
                try {
                    serverConnection = get();
                    setState(serverConnection == null ? new OfflineState() : new OnlineState());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace(); // TODO: добавить повторную попытку
                }
            }
        }
        var serverConnectionThread = new ServerConnectionThread();
        serverConnectionThread.execute();
    }

    private void setState(State newState) {
        state = newState;
    }

    public void addTask(Task task) {
        state.addTask(task);
        view.updateTasks(tasks);
    }

    public void deleteTask(int index) {
        state.deleteTask(index);
        view.updateTasks(tasks);
    }

    public void changeTask(int index, Task newTask) {
        state.changeTask(index, newTask);
        view.updateTasks(tasks);
    }

    public TaskMap getTasks() {
        return tasks;
    }

    public void verifyUser(String uncheckedUser) {
        state.verifyUser(uncheckedUser);
    }

    public void loadUser(String checkedUser) {
        try {
            var p = new Parser();
            p.parse(serverConnection.readUserTasks(checkedUser));
            tasks.putAll(p.tasks);
        } catch (IOException e) {
            return;
        } catch (ParseException e) {
            // TODO: попытаться вновь
        }
    }

    public void close() {
        serverConnection.close();
    }

    // @Override
    // public void finalize() throws Exception {
    // if (!serverConnection.isClosed()) {
    // serverConnection.close();
    // throw new Exception("Model has not been closed");
    // }
    // }
}