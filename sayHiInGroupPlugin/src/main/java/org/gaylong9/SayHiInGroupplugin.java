package org.gaylong9;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.MiraiLogger;
import org.yaml.snakeyaml.Yaml;

import org.gaylong9.SayHiInGroupPluginData.MODE;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class SayHiInGroupplugin extends JavaPlugin {
    public static final SayHiInGroupplugin INSTANCE = new SayHiInGroupplugin();

    private MiraiLogger logger;
    private static final String pluginName = "SayHiInGroupPlugin";
    private static final String projectName = "org.gaylong9.SayHiInGroupPlugin";

    private SayHiInGroupplugin() {
        super(new JvmPluginDescriptionBuilder(projectName, "1.2-RELEASE")
                .name(pluginName)
                .author("jsy")
                .build());
    }

    @Override
    public void onEnable() {
        logger = getLogger();
        logger.info("SayHiInGroupPlugin loaded");
        // logger.info(System.getProperty("user.dir")); // .../mirai-console

        // 引入插件数据
        SayHiInGroupPluginData pluginData = SayHiInGroupPluginData.INSTANCE;
        loadPluginData(pluginData);
        // mirai JAutoSavePluginData有bug，改用Java实现数据保存
        // reloadPluginData(pluginData);
        // MemoryPluginDataStorage.create().store(pluginData::getSaveName, pluginData);
        // MultiFilePluginDataStorage.create(Paths.get(pluginData.getSaveName())).store(pluginData::getSaveName, pluginData);

        // 注册命令
        CommandManager.INSTANCE.registerCommand(SayHiInGroupPluginCommand.INSTANCE, false);

        // 监听群聊消息
        Listener<GroupMessageEvent> listener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, (GroupMessageEvent event) -> {
            // Bot
            Bot bot = event.getBot();
            long botId = bot.getId();

            // 若插件未启动则不执行操作
            if (!pluginData.isRunning) {
                logger.info("bot " + botId + " was at, but sayHiInGroupPlugin is not running.");
                return;
            }

            // 群号码，specific模式下若不在设置群聊中则不生效
            long groupId = event.getGroup().getId();
            if (pluginData.mode == MODE.SPECIFIC && !pluginData.groups.contains(groupId)) {
                logger.info("bot " + botId + " was at, but group " + groupId + " is not in reply list");
                return;
            }

            // 群聊消息发送者
            Member sender = event.getSender();
            // 群聊消息内容
            String content = event.getMessage().serializeToMiraiCode();

            // 手机QQ艾特会在尾部多一个空格，故trim处理
            if (content.trim().equals("[mirai:at:" + botId + "]")) {
                // 组合发送消息并发送
                PlainText text = new PlainText("你好啊，");
                String name = sender.getNameCard();
                if (name.isEmpty()) {
                    name = sender.getNick();
                }
                ThreadLocalRandom random = ThreadLocalRandom.current();
                int bound = Face.names.length;
                int faceId = random.nextInt(0, bound);
                while (Face.names[faceId] == null || Face.names[faceId].equals("表情")) {
                    faceId = random.nextInt(0, bound);
                }
                Face face = new Face(faceId);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(text);
                builder.add(name);
                builder.add(face);
                event.getSubject().sendMessage(builder.build());
            }
        });
    }

    @Override
    public void onDisable() {
        savePluginData(SayHiInGroupPluginData.INSTANCE);
        super.onDisable();
    }

    private void loadPluginData(SayHiInGroupPluginData pluginData) {
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
                savePluginData(pluginData);
                return;
            }
        } catch (IOException e) {
            logger.debug(Arrays.toString(e.getStackTrace()));
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ymlPath))) {
            Map<String, Object> content = yaml.load(reader);
            pluginData.mode = MODE.valueOf(((String) content.get("mode")).toUpperCase(Locale.ROOT));
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

    private void savePluginData(SayHiInGroupPluginData pluginData) {
        Yaml yaml = new Yaml();
        String mclPath = System.getProperty("user.dir");
        String ymlPath = mclPath + "/data/" + projectName + "/" + pluginData.getSaveName() + ".yml";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ymlPath, false))) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("mode", pluginData.mode.toString());
            map.put("isRunning", pluginData.isRunning);
            map.put("groups", pluginData.groups);
            yaml.dump(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}