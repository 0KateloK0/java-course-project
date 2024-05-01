package Model.Loader;

// import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import Common.Task;
import Common.User;

public class FileLoader implements LoaderInterface {
    private String filename;
    private Parser parser;

    public FileLoader(String filename) {
        this.filename = filename;
        this.parser = new Parser();
        this.loadFile();
    }

    private void loadFile() {
        try {
            var tasks_file = new File(this.filename);
            var file_reader = new Scanner(tasks_file);

            String data = "";
            while (file_reader.hasNextLine()) {
                String line = file_reader.nextLine();
                data += line;
            }

            file_reader.close();
            this.parser.parse(data);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, Task> loadTasks() {
        return parser.tasks;
    }

    public HashMap<Integer, User> loadUsers() {
        return parser.users;
    }
}
