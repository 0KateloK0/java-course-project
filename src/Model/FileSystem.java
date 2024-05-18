package Model;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

import Common.TaskMap;
import Common.User;

public class FileSystem {
    private String filename;
    private Parser parser;

    public FileSystem(String filename) {
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

    public TaskMap loadTasks(User user) {
        return parser.tasks;
    }
}
