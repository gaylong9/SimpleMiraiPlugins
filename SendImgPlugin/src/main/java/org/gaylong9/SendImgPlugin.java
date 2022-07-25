package org.gaylong9;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.gaylong9.SendImgPluginUtils.*;

public final class SendImgPlugin extends JavaPlugin {
    public static final SendImgPlugin INSTANCE = new SendImgPlugin();

    private SendImgPluginData pluginData = SendImgPluginData.INSTANCE;

    private MiraiLogger logger;
    private static final String pluginName = "SendImgPlugin";
    private static final String projectName = "org.gaylong9.SendImgPlugin";

    private SendImgPlugin() {
        super(new JvmPluginDescriptionBuilder("org.gaylong9.SendImgPlugin", "1.0-RELEASE")
                .name("SendImgPlugin")
                .info("根据触发词自动从指定目录发送1至threshold张图片")
                .author("gaylong9")
                .build());
    }

    @Override
    public void onEnable() {
        logger = getLogger();

        // 引入插件数据
        loadPluginData();
        SendImgPluginData pluginData = SendImgPluginData.INSTANCE;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 注册命令
        CommandManager.INSTANCE.registerCommand(SendImgPluginCommand.INSTANCE, false);

        // 监听消息
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, (GroupMessageEvent event) -> {
            // Bot
            Bot bot = event.getBot();
            long botId = bot.getId();

            //群聊消息
            String content = event.getMessage().serializeToMiraiCode().trim();

            if (pluginData.triggerWords.contains(content)) {
                File imgDir = new File(pluginData.imgPath);
                String[] imgNames = imgDir.list();
                String hintMessage;
                if (imgNames.length == 0) {
                    event.getSubject().sendMessage("没有库存啦！");
                    return;
                }

                int imgNum = imgNames.length, idx = random.nextInt(0, imgNames.length);;
                int sendNum = Math.min(imgNum, random.nextInt(1, pluginData.threshold + 1));
                for (int i = 0; i < sendNum; i++) {
                    String imgName = imgNames[(idx + i) % imgNum];
                    File img = new File(pluginData.imgPath + "/" + imgName);
                    hintMessage = imgName + "；还剩" + (imgNames.length - 1) + "张库存";
                    ExternalResource resource = ExternalResource.create(img);
                    Image image = event.getSubject().uploadImage(resource);
                    event.getSubject().sendMessage(image);
                    try {
                        resource.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (pluginData.delAfterSend) {
                        img.delete();
                    }
                    event.getSubject().sendMessage(hintMessage);
                }
            }
        });
    }

    @Override
    public void onDisable() {
        SendImgPluginUtils.savePluginData();
        super.onDisable();
    }


}