package Common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import org.json.JSONObject;

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
        try {
            if (cacheFile.createNewFile()) {
                // заполнить его нужной информацией
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
            e.printStackTrace(); // это исключение не должно появиться
        }
        return new CachedData();
    }

    public String getUserTasksFileLocation(String username) {
        return "./clientDB/" + username + ".json";
    }

    public UserData loadUserFile(User user) {
        try {
            var userTasksFile = new File(getUserTasksFileLocation(user.name));
            if (userTasksFile.createNewFile()) {
                var fw = new FileWriter(userTasksFile);
                fw.write(new UserData().toJSONString()); // TODO: костыль
                fw.close();
            } else {
                try {
                    return new UserData().fromJSONObject(new JSONObject(readFile(userTasksFile)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // эта ошибка практически никогда не должна произойти
        }
        return new UserData();
    }
}
