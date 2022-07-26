package org.gaylong9;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;

import java.time.LocalTime;
import java.util.concurrent.RunnableScheduledFuture;

import static org.gaylong9.RemindMoYuPluginUtils.*;

public class RemindMoYuPluginCommand extends JCompositeCommand {
    public static final RemindMoYuPluginCommand INSTANCE = new RemindMoYuPluginCommand();

    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(RemindMoYuPluginCommand.class);
    private final RemindMoYuPluginData pluginData = RemindMoYuPluginData.INSTANCE;

    private RemindMoYuPluginCommand() {
        super(RemindMoYuPlugin.INSTANCE,
                "RemindMoYuPlugin",
                "remindmoyuplugin");
    }

    @SubCommand
    @Description("开启插件")
    public void start(CommandSender sender) {
        // 装载所有任务
        if (pluginData.isRunning) {
            return;
        }
        loadPluginData();
        pluginData.isRunning = true;
        loadAllTasks();
        logger.info("start RemindMoYuPlugin, load all tasks");
    }

    @SubCommand
    @Description("停止插件")
    public void stop(CommandSender sender) {
        // 卸载所有任务
        if (!pluginData.isRunning) {
            return;
        }
        pluginData.isRunning= false;
        for (int i = 0; i < pluginData.tasks.size(); i++) {
            pluginData.executor.remove(pluginData.tasks.get(i));
        }
        pluginData.tasks.clear();
        logger.info("stop RemindMoYuPlugin, unload all tasks");
    }

    @SubCommand({"showgroup"})
    @Description("展示生效群组")
    public void showGroup(CommandSender sender) {
        logger.info(pluginData.groups.toString());
    }

    @SubCommand({"showtime"})
    @Description("展示已设置时间")
    public void showTime(CommandSender sender) {
        if (pluginData.times.isEmpty()) {
            logger.info("there is no send msg time");
        }
        for (int i = 0; i < pluginData.times.size(); i++) {
            logger.info("[" + i + "] " + pluginData.times.get(i));
        }
    }

    @SubCommand({"showmsg"})
    @Description("展示已设置语料")
    public void showContent(CommandSender sender) {
        if (pluginData.msgs.isEmpty()) {
            logger.info("there is no Msg");
        }
        for (int i = 0; i < pluginData.msgs.size(); i++) {
            logger.info("[" + i + "] " + pluginData.msgs.get(i));
        }
    }

    @SubCommand({"showtask"})
    @Description("展示运行中任务")
    public void  showTask(CommandSender sender) {
        if(pluginData.tasks.size() == 0) {
            logger.info("this is no task");
        } else {
            for(RunnableScheduledFuture<?> task : pluginData.tasks) {
                logger.info(task.toString());
            }
        }
    }

    @SubCommand({"addgroup"})
    @Description("添加生效群组")
    public void addGroup(CommandSender sender, @Name("群组ID") String content) {
        long groupId;
        try {
            groupId = Long.parseLong(content);
        } catch (NullPointerException e) {
            logger.info("illegal empty group id");
            return;
        } catch (NumberFormatException e) {
            logger.info("illegal group id: " + content);
            return;
        }
        if (pluginData.groups.contains(groupId)) {
            logger.info("group " + groupId + " already in remind list");
            return;
        }
        pluginData.groups.add(groupId);
        logger.info("add group " + groupId + " success");
    }

    @SubCommand({"removegroup"})
    @Description("移除生效群组")
    public void removeGroup(CommandSender sender, @Name("群组ID") String content) {
        long groupId;
        try {
            groupId = Long.parseLong(content);
        } catch (NullPointerException e) {
            logger.info("illegal empty group id");
            return;
        } catch (NumberFormatException e) {
            logger.info("illegal group id: " + content);
            return;
        }
        pluginData.groups.remove(groupId);
        logger.info("remove group " + groupId + " success");
    }

    @SubCommand({"containgroup"})
    @Description("查询是否已设置过指定群组")
    public void containGroup(CommandSender sender, @Name("群组ID") String content) {
        long groupId;
        try {
            groupId = Long.parseLong(content);
        } catch (NullPointerException e) {
            logger.info("illegal empty group id");
            return;
        } catch (NumberFormatException e) {
            logger.info("illegal group id: " + content);
            return;
        }

        logger.info("send list " + (pluginData.groups.contains(groupId)? "contains ": "does not contain ") + groupId);
    }

    @SubCommand({"addmsg"})
    @Description("添加语料；QQ表情[face:QQ表情编号]或[face:QQ表情名]；换行\\\\n")
    public void addContent(CommandSender sender, @Name("语料") String content) {
        MessageChain chain = parseStringToMessageChain(content);
        if (chain != null) {
            pluginData.msgs.add(content);
            pluginData.messageChains.add(chain);
        }
    }

    @SubCommand({"removemsg"})
    @Description("移除语料，参数为showContent中的编号")
    public void removeContent(CommandSender sender, @Name("语料编号") String content) {
        int idx;
        try {
            idx = Integer.parseInt(content);
        } catch (NumberFormatException e) {
            logger.error(content + "is not a number");
            return;
        }
        if (idx < 0 || idx >= pluginData.msgs.size()) {
            logger.error(content + " should between 0 ~ contents.size");
            return;
        }
        pluginData.msgs.remove(idx);
        pluginData.messageChains.remove(idx);
    }

    @SubCommand({"addtime"})
    @Description("添加时间，格式应为HH:mm，24小时制")
    public void addTime(CommandSender sender, @Name("时间") String content) {
        if (content.length() != 5) {
            logger.error("time format should be HH:mm");
            return;
        }

        int hour, minute;
        try {
            hour = Integer.parseInt(content.substring(0, 2));
            minute = Integer.parseInt(content.substring(3, 5));
        } catch (NumberFormatException e) {
            logger.error(content + "is not a illegal time");
            return;
        }

        if (hour > 23) {
            logger.error("hour should be 0 ~ 23");
            return;
        }

        if (minute > 59) {
            logger.error("minute should be 0 ~ 59");
            return;
        }

        if (pluginData.times.contains(content)) {
            logger.warning(content + " is already in time list");
            return;
        }
        pluginData.times.add(content);
        LocalTime taskTime = LocalTime.of(hour, minute);
        pluginData.localTimes.add(taskTime);
        loadTask(taskTime);
    }

    @SubCommand({"removetime"})
    @Description("移除时间，参数为showTime中的编号")
    public void removeTime(CommandSender sender, @Name("时间编号") String content) {
        int idx;
        try {
            idx = Integer.parseInt(content);
        } catch (NumberFormatException e) {
            logger.error(content + "is not a number");
            return;
        }
        if (idx < 0 || idx >= pluginData.times.size()) {
            logger.error(content + " should between 0 ~ times.size");
            return;
        }
        if (pluginData.isRunning && pluginData.executor != null && pluginData.tasks.size() != 0) {
            pluginData.executor.remove(pluginData.tasks.get(idx));
            pluginData.tasks.remove(idx);
        }
        pluginData.times.remove(idx);
        pluginData.localTimes.remove(idx);
    }

}

