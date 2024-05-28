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

import org.json.JSONStringer;

import java.util.Date;
import java.io.File;
import java.io.FileWriter;

public class Model implements Closeable {
    private TaskMap tasks;
    private ServerConnection serverConnection;
    private User activeUser;
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

        public abstract TaskMap loadActiveUserTasks();

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
        }

        @Override
        public void close() {
            serverConnection.close();
        }

        @Override
        public TaskMap loadActiveUserTasks() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'loadActiveUserTasks'");
        }
    }

    private class OfflineState extends State {
        private FileManager fileManager = new FileManager();

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
            var cachedData = fileManager.loadCacheFile(cacheFile);
            var userMap = cachedData.userMap;
            for (var user : userMap.values()) {
                if (user.name.equals(uncheckedUser)) {
                    return true;
                }
            }
            // try {

            // } catch (ParseException | JSONException e) {
            // // пересоздать файл
            // // cacheFile.delete();
            // e.printStackTrace();
            // System.err.println("JSON error");
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
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

        @Override
        public TaskMap loadActiveUserTasks() {
            var cachedUserData = fileManager.loadUserFile(activeUser);
            ((Task.DefaultIdGenerator) Task.getIdGenerator()).setLastId(cachedUserData.lastTaskId);
            return cachedUserData.tasks;
        }
    }

    private State state;
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("y MM dd HH:mm");

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

    public void close() {
        state.close();
    }
}