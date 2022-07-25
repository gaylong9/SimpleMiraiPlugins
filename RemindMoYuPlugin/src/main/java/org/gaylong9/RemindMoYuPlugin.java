package org.gaylong9;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.MiraiLogger;

import static org.gaylong9.RemindMoYuPluginUtils.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public final class RemindMoYuPlugin extends JavaPlugin {
    public static final RemindMoYuPlugin INSTANCE = new RemindMoYuPlugin();

    private MiraiLogger logger;
    private static final String pluginName = "RemindMoYuPlugin";
    private static final String projectName = "org.gaylong9.RemindMoYuPlugin";

    private RemindMoYuPlugin() {
        super(new JvmPluginDescriptionBuilder(projectName, "1.0-RELEASE")
                .name(pluginName)
                .info("定时提醒摸鱼")
                .author("gaylong9")
                .build());
    }

    @Override
    public void onEnable() {
        logger = getLogger();

        // 引入插件数据
        loadPluginData();
        RemindMoYuPluginData pluginData = RemindMoYuPluginData.INSTANCE;

        // 注册命令
        CommandManager.INSTANCE.registerCommand(RemindMoYuPluginCommand.INSTANCE, false);

        // Bot登陆后执行
        GlobalEventChannel.INSTANCE.subscribeAlways(BotOnlineEvent.class, event -> {
            if (pluginData.hasLoadedTasks) {
                logger.info("has loaded tasks, maybe bot reconnect, will not load");
            } else {
                logger.info("bot " + event.getBot().getId() + " online, will load all tasks");
                loadAllTasks();
                pluginData.hasLoadedTasks = true;
            }
        });


    }

    @Override
    public void onDisable() {
        RemindMoYuPluginUtils.savePluginData();
        RemindMoYuPluginData.INSTANCE.executor.shutdownNow();
        super.onDisable();
    }
}