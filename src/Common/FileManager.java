package Common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONStringer;

import Model.Model;

public class FileManager {
    private String cwd;

    public FileManager(String cwd) {
        this.cwd = cwd;
    }

    private String readFile(File file) throws IOException {
        var scanner = new Scanner(file);
        String res = "";
        while (scanner.hasNextLine()) {
            res += scanner.nextLine();
        }
        scanner.close();
        return res;
    }

    // public <T extends JSONifiable> T loadJSONFile(File file) {
    // try {
    // if (file.createNewFile()) {
    // // заполнить его нужной информацией
    // var fw = new FileWriter(file);
    // fw.write(new T().toJSONString());
    // fw.close();
    // } else {
    // try {
    // return new T().fromJSONObject(new JSONObject(readFile(file)));
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }
    // }
    // } catch (IOException e) {
    // e.printStackTrace(); // это исключение не должно появиться
    // }
    // return new T();
    // }

    public CachedData loadCacheFile(File cacheFile) {
        try {
            if (cacheFile.createNewFile()) {
                var fw = new FileWriter(cacheFile);
                fw.write(new CachedData().toJSONString());
                fw.close();
            } else {
                try {
                    return new CachedData().fromJSONObject(new JSONObject(readFile(cacheFile)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // эта ошибка практически никогда не должна произойти
        }
        return new CachedData();
    }

    public String getUserTasksFileLocation(String username) {
        return cwd + username + ".json";
    }

    public UserData loadUser(User user) {
        return loadUserFile(new File(getUserTasksFileLocation(user.name)));
    }

    public UserData loadUserFile(File userFile) {
        try {
            if (userFile.createNewFile()) {
                var fw = new FileWriter(userFile);
                fw.write(new UserData().toJSONString());
                fw.close();
            } else {
                try {
                    return new UserData().fromJSONObject(new JSONObject(readFile(userFile)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // эта ошибка практически никогда не должна произойти
        }
        return new UserData();
    }

    public void saveModelState(Model model) {
        if (model.getActiveUser() == null)
            return;
        var userTasksFile = new File("./clientDB/" + model.getActiveUser().name + ".json");
        try {
            var fw = new FileWriter(userTasksFile);
            var userData = new UserData();
            userData.tasks = model.getTasks();
            userData.lastTaskId = ((Task.DefaultIdGenerator) Task.getIdGenerator()).getLastId();
            // userData.lastChanged = new Date(); // TODO: костыль
            fw.write(userData.toJSONString());
            fw.close();
            // var obj = new JSONStringer()
            // .object()
            // .key("tasks")
            // .array();
            // for (var task : model.getTasks().values()) {
            // obj.value(task.toJSONObject());
            // }
            // obj.endArray();
            // obj.key("metadata")
            // .object()
            // .key("lastChanged").value(Model.DATE_FORMAT.format(new Date()))
            // .key("lastTaskId")
            // .value(((Task.DefaultIdGenerator) Task.getIdGenerator()).getLastId())
            // .endObject();
            // obj.endObject();
            // fw.write(obj.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
