package org.gaylong9;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.MiraiLogger;

import java.time.LocalTime;
import java.util.Locale;
import java.util.concurrent.RunnableScheduledFuture;

import static org.gaylong9.RemindMoYuPluginUtils.*;

public class RemindMoYuPluginCommand extends JSimpleCommand {
    public static final RemindMoYuPluginCommand INSTANCE = new RemindMoYuPluginCommand();

    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(RemindMoYuPluginCommand.class);
    private final RemindMoYuPluginData pluginData = RemindMoYuPluginData.INSTANCE;

    private RemindMoYuPluginCommand() {
        super(RemindMoYuPlugin.INSTANCE,
                "RemindMoYuPlugin",
                "remindmoyuplugin");
    }

    // 1个命令符
    @Handler()
    public void onCommand(CommandSender sender, String operation) {
        operation = operation.toLowerCase(Locale.ROOT);
        switch (operation) {
            case "start":
                // 装载所有任务
                if (pluginData.isRunning) {
                    return;
                }
                loadPluginData();
                pluginData.isRunning = true;
                loadAllTasks();
                logger.info("start RemindMoYuPlugin, load all tasks");
                break;
            case "stop":
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
                break;
            case "showgroup":
                logger.info(pluginData.groups.toString());
                break;
            case "showtime":
                if (pluginData.times.isEmpty()) {
                    logger.info("there is no send msg time");
                }
                for (int i = 0; i < pluginData.times.size(); i++) {
                    logger.info("[" + i + "] " + pluginData.times.get(i));
                }
                break;
            case "showcontent":
                if (pluginData.contents.isEmpty()) {
                    logger.info("there is no content");
                }
                for (int i = 0; i < pluginData.contents.size(); i++) {
                    logger.info("[" + i + "] " + pluginData.contents.get(i));
                }
                break;
            case "showtask":
                if(pluginData.tasks.size() == 0) {
                    logger.info("this is no task");
                } else {
                    for(RunnableScheduledFuture<?> task : pluginData.tasks) {
                        logger.info(task.toString());
                    }
                }
                break;
            default:
                logger.info("illegal operation");
        }
    }

    // 2个命令符
    @Handler
    public void onCommand(CommandSender sender, String operation, String content) {
        operation = operation.toLowerCase(Locale.ROOT);
        switch (operation) {
            case "addgroup":
                addGroup(content);
                break;
            case "removegroup":
                removeGroup(content);
                break;
            case "containgroup":
                containGroup(content);
                break;
            case "addcontent":
                addContent(content);
                break;
            case "removecontent":
                removeContent(content);
                break;
            case "addtime":
                addTime(content);
                break;
            case "removetime":
                removeTime(content);
                break;
            default:
                logger.info("illegal operation");
        }
    }

    private void addGroup(String content) {
        Long groupId;
        try {
            groupId = Long.valueOf(content);
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

    private void removeGroup(String content) {
        Long groupId;
        try {
            groupId = Long.valueOf(content);
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

    private void containGroup(String content) {
        Long groupId;
        try {
            groupId = Long.valueOf(content);
        } catch (NullPointerException e) {
            logger.info("illegal empty group id");
            return;
        } catch (NumberFormatException e) {
            logger.info("illegal group id: " + content);
            return;
        }

        logger.info("send list " + (pluginData.groups.contains(groupId)? "contains ": "does not contain ") + groupId);
    }

    private void addContent(String content) {
        MessageChain chain = parseStringToMessageChain(content);
        if (chain != null) {
            pluginData.contents.add(content);
            pluginData.messageChains.add(chain);
        }
    }

    private void removeContent(String content) {
        int idx;
        try {
            idx = Integer.parseInt(content);
        } catch (NumberFormatException e) {
            logger.error(content + "is not a number");
            return;
        }
        if (idx < 0 || idx >= pluginData.contents.size()) {
            logger.error(content + " should between 0 ~ contents.size");
            return;
        }
        pluginData.contents.remove(idx);
        pluginData.messageChains.remove(idx);
    }

    private void addTime(String content) {
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

    private void removeTime(String content) {
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

