package Model.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import Common.Task;

public class FileLoader implements LoaderInterface {
    String filename;

    public FileLoader(String filename) {
        this.filename = filename;
    }

    private String loadFile() {
        try {
            var tasks_file = new File(this.filename);
            var file_reader = new Scanner(tasks_file);

            String data = "";
            while (file_reader.hasNextLine()) {
                String line = file_reader.nextLine();
                data += line;
            }

            return data;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public HashMap<Integer, Task> loadTasks() {
        // var tasks = new ArrayList<Task>();
        var tasks = new HashMap<Integer, Task>();
        var parser = new Parser();
        try {
            parser.parse(this.loadFile());
        } catch (ParseException e) {
            e.printStackTrace();
            return new HashMap<>();
        }

        return parser.tasks;
    }
}
