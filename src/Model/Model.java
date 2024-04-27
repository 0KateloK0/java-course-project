package Model;

import Model.Loader.FileLoader;
import Model.Loader.LoaderInterface;
import Common.Task;
// import java.util.ArrayList;
import java.util.HashMap;

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
    HashMap<Integer, Task> tasks;
    // HashMap<int, Task> tasks;

    public Model() {
        this.loader = new FileLoader("db.json");
        var tasks = this.loader.loadTasks();
        for (Task x : tasks.values()) {
            System.out.println(x.id);
            System.out.println(x.name);
            System.out.println(x.description);
            System.out.println(x.deadline);

        }
        this.tasks.putAll(tasks);
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

    public HashMap<Integer, Task> getTasks() {
        return this.tasks;
    }

    public boolean verifyUser(String uncheckedUser) {
        return false;
    }

    public void loadUser(String checkedUser) {

    }
}