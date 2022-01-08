package org.gaylong9;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.Locale;

public class PluginCommand extends JSimpleCommand {
    public static final PluginCommand INSTANCE = new PluginCommand();

    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(PluginCommand.class);
    private final MyPluginData pluginData = MyPluginData.INSTANCE;

    private PluginCommand() {
        super(SayHiInGroupplugin.INSTANCE,
                "sayHiInGroupPlugin",
                "sayhiingroupplugin");
    }

    @Handler
    public void onCommand(CommandSender sender, String operation) {
        operation = operation.toLowerCase(Locale.ROOT);
        switch (operation) {
            case "start":
                pluginData.isRunning = true;
                logger.info("start SayHiInGroupPlugin success");
                break;
            case "stop":
                pluginData.isRunning= false;
                logger.info("stop SayHiInGroupPlugin success");
                break;
            case "showgroupmode":
                logger.info(pluginData.mode);
                break;
            case "showgroup":
                logger.info(pluginData.groups.toString());
                break;
            default:
                logger.info("illegal operation");
        }
    }

    @Handler
    public void onCommand(CommandSender sender, String operation, String content) {
        operation = operation.toLowerCase(Locale.ROOT);
        switch (operation) {
            case "switchgroupmode":
                switchGroupMode(content);
                break;
            case "addgroup":
                addGroup(content);
                break;
            case "removegroup":
                removeGroup(content);
                break;
            case "containgroup":
                containGroup(content);
                break;
            default:
                logger.info("illegal operation");
        }
    }

    private void switchGroupMode(String mode) {
        mode = mode.toLowerCase(Locale.ROOT);
        if (mode.equals("all")) {
            pluginData.mode= "all";
        } else if (mode.equals("specific")) {
            pluginData.mode = "specific";
        } else {
            logger.info("illegal mode param: " + mode + ", should be all or specific");
            return;
        }
        logger.info("switch group mode to " + pluginData.mode);
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
        pluginData.groups.add(groupId);
        boolean isSuccess = pluginData.groups.contains(groupId);
        logger.info("add group " + groupId + (isSuccess? " success": " fail"));
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
        boolean contains = pluginData.groups.contains(groupId);
        logger.info("remove group " + groupId + (contains? " fail": " success"));
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

        logger.info("reply list " + (pluginData.groups.contains(groupId)? "contains ": "does not contain ") + groupId);
    }


}