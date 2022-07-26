package org.gaylong9;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class RepeatPluginCommand extends JCompositeCommand {
    public static final RepeatPluginCommand INSTANCE = new RepeatPluginCommand();

    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(RepeatPluginCommand.class);
    private final RepeatPluginData pluginData = RepeatPluginData.INSTANCE;

    private RepeatPluginCommand() {
        super(RepeatPlugin.INSTANCE,
                "RepeatPlugin",
                "repeatplugin");
    }

    @NotNull
    @Override
    public String getUsage() {
        return super.getUsage();
    }

    @SubCommand
    @Description("开启插件")
    public void start(CommandSender sender) {
        pluginData.isRunning = true;
        logger.info("start RepeatPlugin success");
    }

    @SubCommand
    @Description("停止插件")
    public void stop(CommandSender sender) {
        pluginData.isRunning= false;
        logger.info("stop RepeatPlugin success");
    }

    @SubCommand("showgroupmode")
    @Description("查看生效群组模式，all：所有群组；specific：指定群组")
    public void showGroupMode(CommandSender sender) {
        logger.info(pluginData.mode.toString());
    }

    @SubCommand("showgroup")
    @Description("查看指定群组")
    public void showGroup(CommandSender sender) {
        logger.info(pluginData.groups.toString());
    }

    @SubCommand("switchgroupmode")
    @Description("切换群组模式")
    public void switchGroupMode(CommandSender sender) {
        if (pluginData.mode == RepeatPluginData.MODE.ALL) {
            pluginData.mode = RepeatPluginData.MODE.SPECIFIC;
        } else {
            pluginData.mode = RepeatPluginData.MODE.ALL;
        }
        logger.info("switch group mode to " + pluginData.mode);
    }

    @SubCommand("addgroup")
    @Description("增加指定群组")
    public void addGroup(CommandSender sender, @Name("群组ID") String content) {
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
            logger.info("group " + groupId + " already in reply list");
            return;
        }
        pluginData.groups.add(groupId);
        boolean isSuccess = pluginData.groups.contains(groupId);
        logger.info("add group " + groupId + (isSuccess? " success": " fail"));
    }

    @SubCommand("removegroup")
    @Description("移除指定群组")
    public void removeGroup(CommandSender sender, @Name("群组ID") String content) {
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

    @SubCommand("containgroup")
    @Description("是否已有指定群组")
    public void containGroup(CommandSender sender, @Name("群组ID") String content) {
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