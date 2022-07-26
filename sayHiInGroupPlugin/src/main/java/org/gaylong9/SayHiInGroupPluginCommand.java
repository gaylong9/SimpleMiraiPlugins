package org.gaylong9;

import jdk.jfr.Description;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.utils.MiraiLogger;
import org.gaylong9.SayHiInGroupPluginData.MODE;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class SayHiInGroupPluginCommand extends JCompositeCommand {
    public static final SayHiInGroupPluginCommand INSTANCE = new SayHiInGroupPluginCommand();

    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(SayHiInGroupPluginCommand.class);
    private final SayHiInGroupPluginData pluginData = SayHiInGroupPluginData.INSTANCE;

    private SayHiInGroupPluginCommand() {
        super(SayHiInGroupplugin.INSTANCE,
                "SayHiInGroupPlugin",
                "sayhiingroupplugin");
    }

    @SubCommand
    @Description("开启插件")
    public void start(CommandSender sender) {
        pluginData.isRunning = true;
        logger.info("start SayHiInGroupPlugin success");
    }

    @SubCommand
    @Description("停止插件")
    public void stop(CommandSender sender) {
        pluginData.isRunning= false;
        logger.info("stop SayHiInGroupPlugin success");
    }

    @SubCommand({"showgroupmode"})
    @Description("显示群组模式")
    public void showGroupMode(CommandSender sender) {
        logger.info(pluginData.mode.toString());
    }

    @SubCommand({"showgroup"})
    @Description("展示已设置群组")
    public void showGroup(CommandSender sender) {
        logger.info(pluginData.groups.toString());
    }

    @SubCommand({"switchgroupmode"})
    @Description("切换群组模式")
    public void switchGroupMode(CommandSender sender) {
        if (pluginData.mode == MODE.ALL) {
            pluginData.mode = MODE.SPECIFIC;
        } else {
            pluginData.mode = MODE.ALL;
        }
        logger.info("switch group mode to " + pluginData.mode);
    }

    @SubCommand({"addgroup"})
    @Description("添加群组")
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

    @SubCommand({"removegroup"})
    @Description("移除群组")
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

    @SubCommand({"containgroup"})
    @Description("查询是否已经添加过指定群组")
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

        logger.info("reply list " + (pluginData.groups.contains(groupId)? "contains ": "does not contain ") + groupId);
    }

}