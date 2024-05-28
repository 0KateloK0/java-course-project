package Common;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import Model.Model;

public class FileManager {
    public UserMap loadCacheFile(File cacheFile) {
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
                var userMap = new UserMap();
                var usersJSON = obj.getJSONArray("users");
                for (int i = 0; i < usersJSON.length(); ++i) {
                    var user = new User().fromJSONObject(usersJSON.getJSONObject(i));
                    userMap.put(user.id, user);
                }
                return userMap;
            }
        } catch (Exception e) {

        }
        return new UserMap();
    }

    public TaskMap loadUserFile(User user) {
        try {

            var userTasks = new TaskMap();
            var userTasksFile = new File("./clientDB/" + user.name + ".json");
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
            return userTasks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TaskMap();
    }
}
