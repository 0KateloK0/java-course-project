package Model;

import Common.Task;
import Common.TaskMap;
import Common.User;
import Controller.Controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Closeable;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// import javax.swing.SwingWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Date;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

public class Model implements Closeable {
    TaskMap tasks;
    Controller controller;
    // View view;
    ServerConnection serverConnection;
    User activeUser;
    private PropertyChangeSupport propertyChanger = new PropertyChangeSupport(this);

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    private abstract class State {
        public abstract void addTask(Task task);

        public abstract void deleteTask(int index);

        public abstract void changeTask(int index, Task newTask);

        public abstract boolean verifyUser(String uncheckedUser);

        public abstract void close();
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
        public boolean verifyUser(String uncheckedUser) {
            try {
                return serverConnection.checkUser(uncheckedUser);
            } catch (Exception e) {
                return false;
            }

            // new Thread() {
            // public void run() {
            // try {
            // if (serverConnection.checkUser(uncheckedUser)) {
            // view.loadMainScreen();
            // serverConnection.getUser(uncheckedUser);
            // } else {
            // view.promptUser();
            // }
            // } catch (Exception e) {
            // view.promptUser();
            // }
            // }
            // }.start();
        }

        @Override
        public void close() {
            serverConnection.close();
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
        public boolean verifyUser(String uncheckedUser) {
            var cacheFile = new File("./clientDB/cache.json");
            try {
                if (cacheFile.createNewFile()) {
                    // заполнить его нужной информацией
                    var obj = new JSONObject();
                    obj.put("users", new JSONArray());
                    var fw = new FileWriter(cacheFile);
                    fw.write(obj.toString());
                    fw.close();
                } else {
                    var cacheScanner = new Scanner(cacheFile);
                    String cache = "";
                    while (cacheScanner.hasNextLine()) {
                        String line = cacheScanner.nextLine();
                        cache += line;
                    }

                    cacheScanner.close();

                    var obj = new JSONObject(cache);

                    var usersJSON = obj.getJSONArray("users");
                    for (int i = 0; i < usersJSON.length(); ++i) {
                        var user = new User().fromJSONObject(usersJSON.getJSONObject(i));
                        if (user.name.equals(uncheckedUser)) {
                            setActiveUser(user);
                            var userTasks = new TaskMap();
                            var userTasksFile = new File("./clientDB/" + user.name + ".json");
                            if (userTasksFile.createNewFile()) {
                                var fw = new FileWriter(userTasksFile);
                                fw.write(new JSONStringer()
                                        .object()
                                        .key("tasks").array().endArray()
                                        .key("metadata")
                                        .object()
                                        .key("lastChanged").value(DATE_FORMAT.format(new Date()))
                                        .key("lastTaskId")
                                        .value(((Task.DefaultIdGenerator) Task.getIdGenerator()).getLastId())
                                        .endObject()
                                        .endObject()
                                        .toString());
                                fw.close();
                            } else {
                                var userTasksInput = new Scanner(userTasksFile);
                                String userTasksCache = "";
                                while (userTasksInput.hasNextLine()) {
                                    String line = userTasksInput.nextLine();
                                    userTasksCache += line;
                                }
                                var userTasksObj = new JSONObject(userTasksCache);
                                var tasksJSON = userTasksObj.getJSONArray("tasks");
                                for (int j = 0; j < tasksJSON.length(); ++j) {
                                    var task = new Task().fromJSONObject(tasksJSON.getJSONObject(j));
                                    userTasks.put(task.id, task);
                                }

                                var metadataJSON = userTasksObj.getJSONObject("metadata");
                                var lastTaskId = metadataJSON.getInt("lastTaskId");
                                ((Task.DefaultIdGenerator) Task.getIdGenerator()).setLastId(lastTaskId); // TODO:
                                                                                                         // костыль

                                userTasksInput.close();
                            }

                            // javax.swing.SwingUtilities.invokeLater(new Runnable() {
                            // @Override
                            // public void run() {
                            // view.loadMainScreen();
                            // view.updateTasks(userTasks);
                            // }
                            // });
                            tasks = userTasks;
                            return true;
                        }
                    }
                }
            } catch (ParseException | JSONException e) {
                // пересоздать файл
                // cacheFile.delete();
                e.printStackTrace();
                System.err.println("JSON error");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
            // view.promptUser();
        }

        @Override
        public void close() {
            var userTasksFile = new File("./clientDB/" + activeUser.name + ".json");
            try {
                var fw = new FileWriter(userTasksFile);
                var obj = new JSONStringer()
                        .object()
                        .key("tasks")
                        .array();
                for (var task : tasks.values()) {
                    obj.value(task.toJSONObject());
                }
                obj.endArray();
                obj.key("metadata")
                        .object()
                        .key("lastChanged").value(DATE_FORMAT.format(new Date()))
                        .key("lastTaskId")
                        .value(((Task.DefaultIdGenerator) Task.getIdGenerator()).getLastId())
                        .endObject();
                obj.endObject();
                fw.write(obj.toString());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    State state;
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("y MM dd HH:mm");

    public Model(Controller controller) {
        tasks = new TaskMap();

        this.controller = controller;
        // this.view = view;
        new Thread() {
            public void run() {
                try {
                    serverConnection = new ServerConnection();
                    setState(new OnlineState());
                } catch (IOException e) {
                    setState(new OfflineState());
                }
            }
        }.start();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChanger.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChanger.removePropertyChangeListener(listener);
    }

    private void setState(State newState) {
        var oldState = state;
        state = newState;
        propertyChanger.firePropertyChange("state", oldState instanceof OnlineState, newState instanceof OnlineState);
    }

    public void addTask(Task task) {
        var oldTasks = tasks.clone();
        state.addTask(task);
        propertyChanger.firePropertyChange("tasks", oldTasks, tasks);
    }

    public void deleteTask(int index) {
        var oldTasks = tasks.clone();
        state.deleteTask(index);
        propertyChanger.firePropertyChange("tasks", oldTasks, tasks);
    }

    public void changeTask(int index, Task newTask) {
        var oldTasks = tasks.clone();
        state.changeTask(index, newTask);
        propertyChanger.firePropertyChange("tasks", oldTasks, tasks);
    }

    public TaskMap getTasks() {
        return tasks;
    }

    public boolean verifyUser(String uncheckedUser) {
        return state.verifyUser(uncheckedUser);
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
        state.close();
    }
}