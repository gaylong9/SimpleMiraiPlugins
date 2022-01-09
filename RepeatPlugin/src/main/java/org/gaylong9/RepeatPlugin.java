package org.gaylong9;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.MiraiLogger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public final class RepeatPlugin extends JavaPlugin {
    public static final RepeatPlugin INSTANCE = new RepeatPlugin();

    private MiraiLogger logger;
    private static final String pluginName = "RepeatPlugin";
    private static final String projectName = "org.gaylong9.RepeatPlugin";


    private RepeatPlugin() {
        super(new JvmPluginDescriptionBuilder(projectName, "1.0-RELEASE")
                .name(pluginName)
                .info("复读")
                .author("gaylong9")
                .build());
    }

    @Override
    public void onEnable() {
        logger = getLogger();
        logger.info("Plugin loaded!");

        // 引入插件数据
        RepeatPluginData pluginData = RepeatPluginData.INSTANCE;
        loadPluginData();

        // 注册命令
        CommandManager.INSTANCE.registerCommand(RepeatPluginCommand.INSTANCE, false);

        // 监听群聊消息
        Listener<GroupMessageEvent> listener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, (GroupMessageEvent event) -> {
            // Bot
            Bot bot = event.getBot();
            long botId = bot.getId();

            // 若插件未启动则不执行操作
            if (!pluginData.isRunning) {
                return;
            }
            // 群号码，specific模式下若不在设置群聊中则不生效
            long groupId = event.getGroup().getId();
            if (pluginData.mode.equals("specific") && !pluginData.groups.contains(groupId)) {
                return;
            }

            // 群聊消息
            // 获取的是MessageChain，剔除MessageMetaData，仅保留可见消息
            MessageChain receiveChain = event.getMessage();
            MessageChainBuilder builder = new MessageChainBuilder();
            for (int i = 1; i < receiveChain.size(); i++) {
                builder.add(receiveChain.get(i));
            }
            MessageChain msg = builder.build();

            if (msg.equals(pluginData.lastReceive)) {
                if (!msg.equals(pluginData.lastSend)) {
                    // 别人在复读而bot还未参与时，加入复读
                    event.getSubject().sendMessage(msg);
                    pluginData.lastSend = msg;
                }
            } else {
                // 别人没有复读 或 开启新话题，清空上次发送记录
                // 以便 当群友复读之前内容时能再次加入复读
                pluginData.lastSend = pluginData.emptyMsg;
            }
            pluginData.lastReceive = msg;

        });
    }

    @Override
    public void onDisable() {
        savePluginData();
        super.onDisable();
    }

    private void loadPluginData() {
        RepeatPluginData pluginData = RepeatPluginData.INSTANCE;
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
            Map<String, Object> content = yaml.load(reader);
            pluginData.mode = (String) content.get("mode");
            pluginData.isRunning = (Boolean) content.get("isRunning");
            List<?> rawGroups = (ArrayList<?>) content.get("groups");
            for (Object rawGroup : rawGroups) {
                pluginData.groups.add(((Integer) rawGroup).longValue());
            }
            logger.info("mode: " + pluginData.mode);
            logger.info("isRunning: " + pluginData.isRunning);
            logger.info("groups: " + pluginData.groups.toString());
        } catch (IOException e) {
            logger.info("data Yaml does not exist, load PluginData failed");
        }
    }

    private void savePluginData() {
        RepeatPluginData pluginData = RepeatPluginData.INSTANCE;
        Yaml yaml = new Yaml();
        String mclPath = System.getProperty("user.dir");
        String ymlPath = mclPath + "/data/" + projectName + "/" + pluginData.getSaveName() + ".yml";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ymlPath, false))) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("mode", pluginData.mode);
            map.put("isRunning", pluginData.isRunning);
            map.put("groups", pluginData.groups);
            yaml.dump(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}