package org.gaylong9;

import net.mamoe.mirai.utils.MiraiLogger;
import org.yaml.snakeyaml.Yaml;
import sun.nio.cs.UTF_8;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SendImgPluginUtils {

    private static final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(SendImgPluginUtils.class);
    private static final String projectName = "org.gaylong9.SendImgPlugin";

    private static final SendImgPluginData pluginData = SendImgPluginData.INSTANCE;
    
    /**
     * 从yaml中加载数据
     */
    static void loadPluginData() {
        Yaml yaml = new Yaml();
        String mclPath = System.getProperty("user.dir");
        String pluginDataFolderPath = mclPath + "/data/" + projectName;
        String ymlPath = pluginDataFolderPath + "/" + pluginData.getSaveName() + ".yml";

        try {
            File pluginDataFolder = new File(pluginDataFolderPath);
            logger.info("plugin data folder path: " + pluginDataFolderPath + ", exist " + pluginDataFolder.exists());
            if (!pluginDataFolder.exists()) {
                logger.info("plugin data folder does not exist, will create");
                boolean buildSucc = pluginDataFolder.mkdir();
                logger.info(buildSucc? "create success": "create fail");
                if (!buildSucc) {
                    return;
                }
            }

            File ymlFile = new File(ymlPath);
            logger.info("yml path: " + ymlPath + ", exist " + ymlFile.exists());
            if (!ymlFile.exists()) {
                logger.info("yml data does not exist, will create new one");
                boolean buildSucc = ymlFile.createNewFile();
                logger.info(buildSucc? "create success": "create fail");
                savePluginData();
                return;
            }
        } catch (IOException e) {
            logger.debug(Arrays.toString(e.getStackTrace()));
            return;
        }

//        try (BufferedReader reader = new BufferedReader(new FileReader(ymlPath))) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(ymlPath), StandardCharsets.UTF_8)) {
            Map<String, Object> map = yaml.load(reader);
            pluginData.imgPath = (String) map.get("imgPath");
            pluginData.delAfterSend = (Boolean) map.get("delAfterSend");
            pluginData.triggerWords.clear();
            Set<?> rawTriggers = (HashSet<?>) map.get("triggerWords");
            for (Object rawTrigger : rawTriggers) {
                pluginData.triggerWords.add((String) rawTrigger);
            }

            logger.info("imgPath: " + pluginData.imgPath);
            logger.info("delAfterSend: " + pluginData.delAfterSend);
            logger.info("triggerWords: " + pluginData.triggerWords.toString());
        } catch (IOException e) {
            logger.error("data Yaml does not exist, load PluginData failed");
        }
    }

    /**
     * 保存数据至yaml
     */
    static void savePluginData() {
        Yaml yaml = new Yaml();
        String mclPath = System.getProperty("user.dir");
        String pluginDataFolderPath = mclPath + "/data/" + projectName;
        String ymlPath = pluginDataFolderPath + "/" + pluginData.getSaveName() + ".yml";
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ymlPath, ,false))) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ymlPath, false), StandardCharsets.UTF_8)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("imgPath", pluginData.imgPath);
            map.put("delAfterSend", pluginData.delAfterSend);
            map.put("triggerWords", pluginData.triggerWords);
            yaml.dump(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新图片目录，如更改图片路径或新加入图片后，重新加载
     */
    static File refreshImgDir() {
        File dir;
        if (pluginData.imgPath != null && !pluginData.imgPath.equals("")) {
            dir = new File(pluginData.imgPath);
        } else {
            logger.error("img path is illegal");
            return null;
        }
        dir = new File(pluginData.imgPath);
        logger.info("now img num: " + Objects.requireNonNull(dir.list()).length);
        return dir;
    }
}
