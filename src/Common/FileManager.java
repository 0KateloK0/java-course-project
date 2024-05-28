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
    private String readFile(File file) throws IOException {
        var scanner = new Scanner(file);
        String res = "";
        while (scanner.hasNextLine()) {
            res += scanner.nextLine();
        }
        scanner.close();
        return res;
    }

    public CachedData loadCacheFile(File cacheFile) {
        var cachedData = new CachedData();
        try {
            if (cacheFile.createNewFile()) {
                // заполнить его нужной информацией
                var fw = new FileWriter(cacheFile);
                fw.write(new JSONStringer()
                        .object()
                        .key("users").array().endArray()
                        .endObject()
                        .toString());
                fw.close();
            } else {
                var obj = new JSONObject(readFile(cacheFile));
                var userMap = new UserMap();
                var usersJSON = obj.getJSONArray("users");
                for (int i = 0; i < usersJSON.length(); ++i) {
                    try {

                        var user = new User().fromJSONObject(usersJSON.getJSONObject(i));
                        userMap.put(user.id, user);
                    } catch (ParseException e) {
                        e.printStackTrace(); // TODO: пометить этого юзера как некорректного
                    }
                }

                cachedData.userMap = userMap;
            }
        } catch (IOException e) {
            e.printStackTrace(); // это исключение не должно появиться
        }
        return cachedData;
    }

    public String getUserTasksFileLocation(String username) {
        return "./clientDB/" + username + ".json";
    }

    public CachedUserData loadUserFile(User user) {
        var res = new CachedUserData();
        try {
            var userTasksFile = new File(getUserTasksFileLocation(user.name));
            if (userTasksFile.createNewFile()) {
                var fw = new FileWriter(userTasksFile);
                fw.write(new JSONStringer()
                        .object()
                        .key("tasks").array().endArray()
                        .key("metadata")
                        .object()
                        .key("lastChanged").value(Model.DATE_FORMAT.format(new Date()))
                        .key("lastTaskId")
                        .value(((Task.DefaultIdGenerator) Task.getIdGenerator()).getLastId())
                        .endObject()
                        .endObject()
                        .toString());
                fw.close();
            } else {
                var userTasksObj = new JSONObject(readFile(userTasksFile));
                var tasksJSON = userTasksObj.getJSONArray("tasks");
                for (int j = 0; j < tasksJSON.length(); ++j) {
                    try {
                        var task = new Task().fromJSONObject(tasksJSON.getJSONObject(j));
                        res.tasks.put(task.id, task);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // TODO: пометить это задание как задание с ошибкой
                    }
                }

                var metadataJSON = userTasksObj.getJSONObject("metadata");
                res.lastTaskId = metadataJSON.getInt("lastTaskId");
            }
        } catch (IOException e) {
            e.printStackTrace(); // эта ошибка практически никогда не должна произойти
        }
        return res;
    }
}
