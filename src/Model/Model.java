package Model;

import Common.FileManager;
import Common.Task;
import Common.TaskMap;
import Common.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Closeable;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.File;

public class Model implements Closeable {
    private TaskMap tasks;
    private ServerConnection serverConnection;
    private User activeUser;
    private PropertyChangeSupport propertyChanger = new PropertyChangeSupport(this);

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        var oldUser = this.activeUser;
        this.activeUser = activeUser;
        propertyChanger.firePropertyChange("user", oldUser, this.activeUser);
    }

    private abstract class State {
        public abstract void createTask(Task task);

        public abstract void deleteTask(int index);

        public abstract void updateTask(int index, Task newTask);

        public abstract User authenticate(String uncheckedUser);

        public abstract User register(String unregisteredUser);

        public abstract void close();
    }

    private class OnlineState extends State {
        private FileManager fileManager = new FileManager("./clientDB/");

        @Override
        public void createTask(Task task) {
            try {
                serverConnection.createTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deleteTask(int index) {
            try {
                serverConnection.deleteTask(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateTask(int index, Task newTask) {
            try {
                serverConnection.updateTask(index, newTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public User authenticate(String uncheckedUser) {
            try {
                var user = serverConnection.authenticate(uncheckedUser);
                if (user == null)
                    return null;
                setActiveUser(user);

                var serverUserData = serverConnection.readUserTasks(user);
                var cachedUserData = fileManager.loadUser(user);
                if (cachedUserData.lastChanged.after(serverUserData.lastChanged)) {
                    ((Task.DefaultIdGenerator) Task.getIdGenerator()).setLastId(cachedUserData.lastTaskId);
                    tasks = cachedUserData.tasks;
                    serverConnection.syncronise(Model.this);
                } else {
                    ((Task.DefaultIdGenerator) Task.getIdGenerator()).setLastId(serverUserData.lastTaskId);
                    tasks = serverUserData.tasks;
                }

                return user;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void close() {
            try {
                serverConnection.saveModelStateAndClose(Model.this);
                fileManager.saveModelState(Model.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public User register(String unregisteredUser) {
            serverConnection.createUser(unregisteredUser);
            return null; // TODO:
        }
    }

    private class OfflineState extends State {
        private FileManager fileManager = new FileManager("./clientDB/");

        @Override
        public void createTask(Task task) {
        }

        @Override
        public void deleteTask(int index) {
        }

        @Override
        public void updateTask(int index, Task newTask) {
        }

        @Override
        public User authenticate(String uncheckedUser) {
            var cacheFile = new File("./clientDB/cache.json");
            var cachedData = fileManager.loadCacheFile(cacheFile);
            var users = cachedData.users;
            var user = users.readUser(uncheckedUser);
            if (user == null)
                return null;
            setActiveUser(user);

            var cachedUserData = fileManager.loadUser(activeUser);
            ((Task.DefaultIdGenerator) Task.getIdGenerator()).setLastId(cachedUserData.lastTaskId);
            tasks = cachedUserData.tasks;

            return user;
        }

        @Override
        public void close() {
            fileManager.saveModelState(Model.this);
        }

        @Override
        public User register(String unregisteredUser) {
            return null; // Нельзя зарегистрироваться в офлайн состоянии
        }
    }

    private State state;
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("y MM dd HH:mm.ss");

    public Model() {
        tasks = new TaskMap();
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
        var oldState = state instanceof OnlineState;
        state = newState;
        propertyChanger.firePropertyChange("state", oldState, state instanceof OnlineState);
    }

    public boolean isOnline() {
        return state instanceof OnlineState;
    }

    public void addTask(Task task) {
        var oldTasks = tasks.clone();
        state.createTask(task);
        tasks.createTask(task);
        propertyChanger.firePropertyChange("tasks", oldTasks, tasks);
    }

    public void deleteTask(int index) {
        var oldTasks = tasks.clone();
        state.deleteTask(index);
        tasks.deleteTask(index);
        propertyChanger.firePropertyChange("tasks", oldTasks, tasks);
    }

    public void changeTask(int index, Task newTask) {
        var oldTasks = tasks.clone();
        state.updateTask(index, newTask);
        tasks.updateTask(newTask);
        propertyChanger.firePropertyChange("tasks", oldTasks, tasks);
    }

    public User register(String unregisteredUser) {
        return state.register(unregisteredUser);
    }

    public TaskMap getTasks() {
        return tasks;
    }

    public User authenticate(String uncheckedUser) {
        var user = state.authenticate(uncheckedUser);
        if (user == null)
            return null;
        else {
            setActiveUser(user);
            return user;
        }
    }

    public void close() {
        state.close();
    }
}