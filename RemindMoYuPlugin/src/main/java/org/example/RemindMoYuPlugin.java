package org.example;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.MiraiLogger;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RemindMoYuPlugin extends JavaPlugin {
    public static final RemindMoYuPlugin INSTANCE = new RemindMoYuPlugin();

    private RemindMoYuPlugin() {
        super(new JvmPluginDescriptionBuilder("org.example.remindMoYuPlugin", "1.0-SNAPSHOT")
                .name("RemindMoYuPlugin")
                .info("每天10点、4点提醒摸鱼")
                .author("gaylong9")
                .build());
    }

    @Override
    public void onEnable() {
        MiraiLogger logger = getLogger();
        logger.info("RemindMoYuPlugin loaded!");

        AtomicBoolean started = new AtomicBoolean(false);

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            int secondsPerDay = 86400;
            long botId = 2012391808;
            String content = event.getMessage().serializeToMiraiCode();
            if (content.contains("[mirai:at:" + botId + "]")) {
                if (content.contains("start RemindMoYuPlugin")) {
                    if (started.get()) {
                        return;
                    }
                    started.set(true);

                    logger.info("start RemindMoYuPlugin");

                    Group group = event.getSubject();
                    group.sendMessage("RemindMoYuPlugin 启动成功");

                    String msgAt10 = "摸鱼小助手提醒您：\n" +
                            "钱是老板的，命是自己的\n" +
                            "又到了上午10点，工作再累，一定不要忘记摸鱼哦！\n" +
                            "接水喝茶上厕所，发呆提肛看风景\n" +
                            "愿大家今天也能有个好心情！";
                    String msgAt16 = "摸鱼小助手提醒您：\n" +
                            "钱是老板的，命是自己的\n" +
                            "又到了下午4点，今天的工作快要结束了哟！\n" +
                            "接水喝茶上厕所，发呆提肛看风景\n" +
                            "愿大家每天都能有个好心情！";

                    MessageChainBuilder builderAt10 = new MessageChainBuilder();
                    builderAt10.append(msgAt10);
                    builderAt10.append(new Face(Face.CHA));

                    MessageChainBuilder builderAt16 = new MessageChainBuilder();
                    builderAt16.append(msgAt16);
                    builderAt16.append(new Face(Face.QING_ZHU));

                    LocalTime now = LocalTime.now();
                    LocalTime time10 = LocalTime.of(10, 0, 0);
                    LocalTime time16 = LocalTime.of(16, 0, 0);

                    // 定时上午10点提醒
                    int toNext10;
                    if (now.isBefore(time10)) {
                        toNext10 = time10.toSecondOfDay() - now.toSecondOfDay();
                        logger.info("now is " + now + ", before " + time10 + ", need " + toNext10 + "s to next 10.00");
                    } else {
                        toNext10 = secondsPerDay - now.toSecondOfDay() + time10.toSecondOfDay();
                        logger.info("now is " + now + ", after " + time10 + ", need " + toNext10 + "s to next 10.00");
                    }
                    Runnable remindAt10 = () -> group.sendMessage(builderAt10.asMessageChain());

                    // 定时下午4点（16点）提醒
                    int toNext16;
                    if (now.isBefore(time16)) {
                        toNext16 = time16.toSecondOfDay() - now.toSecondOfDay();
                        logger.info("now is " + now + ", before " + time16 + ", need " + toNext16 + "s to next 16.00");

                    } else {
                        toNext16 = secondsPerDay - now.toSecondOfDay() + time16.toSecondOfDay();
                        logger.info("now is " + now + ", after " + time16 + ", need " + toNext16 + "s to next 16.00");

                    }
                    Runnable remindAt16 = () -> group.sendMessage(builderAt16.asMessageChain());

                    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                    executor.scheduleAtFixedRate(remindAt10, toNext10, secondsPerDay, TimeUnit.SECONDS);
                    executor.scheduleAtFixedRate(remindAt16, toNext16, secondsPerDay, TimeUnit.SECONDS);
                }
            }
        });


    }
}