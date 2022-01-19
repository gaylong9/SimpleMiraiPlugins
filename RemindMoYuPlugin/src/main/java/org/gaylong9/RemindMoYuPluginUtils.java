package org.gaylong9;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.MiraiLogger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemindMoYuPluginUtils {

    private static final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(RemindMoYuPluginUtils.class);
    private static final String projectName = "org.gaylong9.RemindMoYuPlugin";

    private static final RemindMoYuPluginData pluginData = RemindMoYuPluginData.INSTANCE;

    static MessageChain parseStringToMessageChain(String content) {
        content = content.replace("\\n", "\n");
//        String timeRegex = "\\[time]";
        String faceRegex = "\\[face:.*?]";

        // 记录各部分内容的起止位置
//        List<Integer> timeStartIdxs = new LinkedList<>();
        List<Integer> faceStartIdxs = new LinkedList<>();
        List<Integer> faceEndIdxs = new LinkedList<>();

        // 查找各部分内容起止位置
        Pattern facePattern = Pattern.compile(faceRegex);
        Matcher matcher = facePattern.matcher(content);
        while (matcher.find()) {
            faceStartIdxs.add(matcher.start(0));
            faceEndIdxs.add(matcher.end(0));
        }

        // append一个非法位置，方便后续操作
        faceStartIdxs.add(content.length());

        // 构造MessageChain
        MessageChainBuilder builder = new MessageChainBuilder();
        int curIdx = 0;
        while (curIdx < content.length()) {
            if (curIdx == faceStartIdxs.get(0)) {
                int faceId;
                String idString = content.substring(curIdx + 6, faceEndIdxs.get(0) - 1);
                if (pluginData.faceId.containsKey(idString)) {
                    faceId = pluginData.faceId.get(idString);
                } else {
                    try {
                        faceId = Integer.parseInt(idString);
                        if (Face.names[faceId] == null || Face.names[faceId].equals("表情")) {
                            logger.error("face " + idString + " does not exist");
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        logger.error("pluginData.faceId does not contain " + idString + ", and it is not a number");
                        return null;
                    }
                }
                builder.append(new Face(faceId));
                curIdx = faceEndIdxs.get(0);
                faceStartIdxs.remove(0);
                faceEndIdxs.remove(0);
            } else {
//                int toIdx = Math.min(faceStartIdxs.get(0), timeStartIdxs.get(0));
                int toIdx = faceStartIdxs.get(0);
                builder.append(content.substring(curIdx, toIdx));
                curIdx = toIdx;
            }
        }

        return builder.build();
    }

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

        try (BufferedReader reader = new BufferedReader(new FileReader(ymlPath))) {
            Map<String, Object> map = yaml.load(reader);
            // isRunning
            pluginData.isRunning = (Boolean) map.get("isRunning");
            // groups
            pluginData.groups.clear();
            List<?> rawGroups = (ArrayList<?>) map.get("groups");
            for (Object rawGroup : rawGroups) {
                pluginData.groups.add(((Integer) rawGroup).longValue());
            }
            // times 和 localTimes
            pluginData.times.clear();
            pluginData.localTimes.clear();
            List<?> rawTimes = (ArrayList<?>) map.get("times");
            for (Object rawTime : rawTimes) {
                String time = (String) rawTime;
                pluginData.times.add(time);
                pluginData.localTimes.add(
                        LocalTime.of(Integer.parseInt(time.substring(0, 2)),
                                    Integer.parseInt(time.substring(3, 5)))
                );
            }
            // content 和 messageChains
            pluginData.contents.clear();
            pluginData.messageChains.clear();
            List<?> rawContents = (ArrayList<?>) map.get("contents");
            for (Object rawContent : rawContents) {
                String content = (String) rawContent;
                pluginData.contents.add(content);
                pluginData.messageChains.add(parseStringToMessageChain(content));
            }

            logger.info("isRunning: " + pluginData.isRunning);
            logger.info("groups: " + pluginData.groups.toString());
            logger.info("times: " + pluginData.times.toString());
            logger.info("contents: " + pluginData.contents.toString());
        } catch (IOException e) {
            logger.error("data Yaml does not exist, load PluginData failed");
        }
    }

    static void savePluginData() {
        Yaml yaml = new Yaml();
        String mclPath = System.getProperty("user.dir");
        String ymlPath = mclPath + "/data/" + projectName + "/" + pluginData.getSaveName() + ".yml";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ymlPath, false))) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("isRunning", pluginData.isRunning);
            map.put("groups", pluginData.groups);
            map.put("contents", pluginData.contents);
            map.put("times", pluginData.times);
            yaml.dump(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadAllTasks() {
        if (!pluginData.isRunning) {
            logger.warning("RemindMoYuPlugin is not running");
            return;
        }
        for (LocalTime taskTime : pluginData.localTimes) {
            loadTask(taskTime);
        }
    }

    static void loadTask(LocalTime taskTime) {
        int secondsPerDay = 86400;
        Bot bot = Bot.getInstances().get(0);
        LocalTime now = LocalTime.now();

        if (!pluginData.isRunning) {
            return;
        }

        // 初始化线程池
        if (pluginData.executor == null) {
            pluginData.executor = new ScheduledThreadPoolExecutor(2);
            pluginData.executor.setMaximumPoolSize(10);
        }

        // 添加任务
        Runnable task = () -> {
            // 从语料库中随机选择一个消息发送至所有指定群聊
            if (pluginData.messageChains.size() == 0) {
                logger.warning("available send content is empty");
                return;
            }
            if (pluginData.groups.size() == 0) {
                logger.warning("available group is empty");
                return;
            }

            ThreadLocalRandom random = ThreadLocalRandom.current();
            int messageChainIdx = random.nextInt(0, pluginData.messageChains.size());
            MessageChain messageChain = pluginData.messageChains.get(messageChainIdx);

            for (int j = 0; j < pluginData.groups.size(); j++) {
                Long groupId = pluginData.groups.get(j);
                Group group = bot.getGroup(groupId);
                if (group != null) {
                    group.sendMessage(messageChain);
                } else {
                    logger.warning("group " + groupId + " is not exist");
                }
            }
        };

        // 初次延迟时长
        int initialDelay;
        if (now.isBefore(taskTime)) {
            // 若任务在今天稍后时间，则直接用当天所在的秒数相减
            initialDelay = taskTime.toSecondOfDay() - now.toSecondOfDay();
            logger.info("now is " + now + ", before " + taskTime + ", need " + initialDelay + "s");
        } else {
            // 若任务在今天稍前时间，即要到第二天才能首次执行
            initialDelay = secondsPerDay - now.toSecondOfDay() + taskTime.toSecondOfDay();
            logger.info("now is " + now + ", after " + taskTime + ", need " + initialDelay + "s");
        }

        pluginData.tasks.add(
                (RunnableScheduledFuture<?>) pluginData.executor.scheduleAtFixedRate(task, initialDelay, secondsPerDay, TimeUnit.SECONDS)
        );
    }
}
