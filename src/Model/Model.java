package Model;

import Model.Loader.FileLoader;
import Model.Loader.LoaderInterface;
import Common.Task;
import Common.TaskMap;
import Common.User;
import Controller.Controller;

// import java.util.ArrayList;
import java.util.HashMap;

import View.View;

// мне нужно загружать и выгружать как-то дела
// хранить их как-то
// хранить их историю изменения
// собсна изменять их
// при этом поддерживать множество способов изменения
// поддерживать также сортировочки и фильтрацию
// в целом хочется ctrl+Z команды обрабатывать

// Итого: паттерн команды для общения между моделью и видом
// лоадер для подгрузки дел. сериализатор понадодбится

public class Model {
    LoaderInterface loader;
    TaskMap tasks;
    HashMap<Integer, User> users;
    Controller controller;
    View view;

    public Model(Controller controller, View view) {
        this.tasks = new TaskMap();
        this.users = new HashMap<>();
        this.loader = new FileLoader("db.json");
        this.controller = controller;
        this.view = view;

        this.tasks.putAll(this.loader.loadTasks());
        this.users.putAll(this.loader.loadUsers());

    }

    public void addTask(Task task) {
        this.tasks.put(task.id, task);
    }

    public void deleteTask(int index) {
        this.tasks.remove(index);
    }

    public void changeTask(int index, Task newTask) {
        this.tasks.put(index, newTask);
    }

    public TaskMap getTasks() {
        return this.tasks;
    }

    public boolean verifyUser(String uncheckedUser) {
        for (var user : this.users.values()) {
            if (user.name.equals(uncheckedUser))
                return true;
        }
        return false;
    }

    public void loadUser(String checkedUser) {

    }
}