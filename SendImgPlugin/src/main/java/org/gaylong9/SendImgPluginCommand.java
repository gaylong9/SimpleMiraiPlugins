package org.gaylong9;

import com.sun.tools.javac.util.StringUtils;
import jdk.internal.joptsimple.internal.Strings;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

public class SendImgPluginCommand extends JCompositeCommand {
    public static final SendImgPluginCommand INSTANCE = new SendImgPluginCommand();
    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(SendImgPluginCommand.class);
    private final SendImgPluginData pluginData = SendImgPluginData.INSTANCE;

    private SendImgPluginCommand() {
        super(SendImgPlugin.INSTANCE,
                "SendImgPlugin",
                "sendimgplugin");
    }

    @SubCommand({"showtrigger"})
    @Description("展示触发词")
    public void showTrigger(CommandSender sender) {
        if (pluginData.triggerWords.isEmpty()) {
            logger.info("there is no content");
        }
        int idx = 1;
        for (String word : pluginData.triggerWords) {
            logger.info("[" + idx + "] " + word);
            idx++;
        }
    }

    @SubCommand({"showimgpath"})
    @Description("展示图片所在目录路径")
    public void showImgPath(CommandSender sender) {
        logger.info(pluginData.imgPath);
    }

    @SubCommand({"showimgnum"})
    @Description("展示图片剩余数量")
    public void showImgNum(CommandSender sender) {
        File imgPath = new File(pluginData.imgPath);
        if (!imgPath.exists()) {
            logger.error("img path is not exist");
            return;
        }
        if (!imgPath.isDirectory()) {
            logger.error("img path is not directory");
            return;
        }
        logger.info(String.valueOf(Objects.requireNonNull(imgPath.list()).length));
    }

    @SubCommand({"showdefflag"})
    @Description("展示是否发送后删除图片文件")
    public void showDelFlag(CommandSender sender) {
        logger.info(String.valueOf(pluginData.delAfterSend));
    }

    @SubCommand({"addtrigger"})
    @Description("添加触发词")
    public void addTrigger(CommandSender sender, @Name("触发词") String content) {
        pluginData.triggerWords.add(content);
    }

    @SubCommand({"removetrigger"})
    @Description("移除触发词")
    public void removeTrigger(CommandSender sender, @Name("触发词") String content) {
        pluginData.triggerWords.remove(content);
    }

    @SubCommand({"setimgpath"})
    @Description("设置图片目录")
    public void setImgPath(CommandSender sender, @Name("图片目录路径") String content) {
        File dir = new File(content);
        if (!dir.exists() || !dir.isDirectory()) {
            logger.error("img path is illegal");
        } else {
            pluginData.imgPath = content;
        }
    }

    @SubCommand({"setdelflag"})
    @Description("设置发送后是否删除，参数应为true或false")
    public void setDelFlag(CommandSender sender, @Name("是否删除") String content) {
        content = content.toLowerCase(Locale.ROOT);

        if ("true".equals(content) || "false".equals(content)) {
            logger.info("should be true or false");
            return;
        }

        pluginData.delAfterSend = Boolean.parseBoolean(content.toLowerCase(Locale.ROOT));
    }

    @SubCommand({"setthreshold"})
    @Description("设置每次发送图片的数量上限")
    public void setThreshold(CommandSender sender, @Name("数量上限") String content) {
        try {
            pluginData.threshold = Integer.parseInt(content);
        } catch (NumberFormatException e) {
            logger.error("should be number");
        }
    }

}
