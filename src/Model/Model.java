package Model;

import Model.Loader.FileLoader;
// import Model.Loader.NetLoader;
import Model.Loader.Parser;
import Model.NetModel.ServerConnection;
import Common.Task;
import Common.TaskMap;
import Common.User;
import Controller.Controller;

import java.io.IOException;
import java.net.*;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import View.View;

public class Model {
    TaskMap tasks;
    Controller controller;
    View view;
    public ServerConnection serverConnection;
    User activeUser;

    public Model(Controller controller, View view) {
        this.tasks = new TaskMap();

        this.controller = controller;
        this.view = view;

        try {
            var thr = new SwingWorker<ServerConnection, Void>() {
                @Override
                protected ServerConnection doInBackground() {
                    try {
                        return new ServerConnection();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };

            // var thr2 = new SwingWorker<TaskMap, Void>() {
            // @Override
            // protected TaskMap doInBackground() {
            // var loader = new FileLoader("db.json");
            // return loader.loadTasks(new User());
            // }
            // };

            thr.execute();
            // thr2.execute();
            this.serverConnection = thr.get();
            // this.tasks.putAll(thr2.get());
            // view.updateTasks(this.tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void addTask(Task task) {
        this.tasks.put(task.id, task);
        // try {
        // this.serverConnection.createNewTask(task);
        // } catch (IOException e) {
        // }

        this.view.updateTasks(tasks);
    }

    public void deleteTask(int index) {
        this.tasks.remove(index);
        this.view.updateTasks(tasks);
    }

    public void changeTask(int index, Task newTask) {
        this.tasks.put(index, newTask);
        this.view.updateTasks(tasks);
    }

    public TaskMap getTasks() {
        return this.tasks;
    }

    public void verifyUser(String uncheckedUser) {
        var thr = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    return serverConnection.checkUser(uncheckedUser);
                } catch (IOException e) {
                    System.out.println("IO");
                    e.printStackTrace();
                    return true;
                } catch (NullPointerException e) {
                    System.out.println("Null");
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
        };
        thr.execute();

        try {
            if (thr.get()) {
                view.loadMainScreen();
            } else {
                view.promptUser();
            }
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void loadUser(String checkedUser) {
        try {
            var p = new Parser();
            p.parse(this.serverConnection.getUserInfo(checkedUser));
            tasks.putAll(p.tasks);
        } catch (IOException e) {
            return;
        } catch (ParseException e) {
            // TODO: попытаться вновь
        }
    }
}