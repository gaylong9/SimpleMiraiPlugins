package org.gaylong9;

import com.sun.tools.javac.util.StringUtils;
import jdk.internal.joptsimple.internal.Strings;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.File;
import java.util.Locale;
import java.util.Objects;

public class SendImgPluginCommand extends JSimpleCommand {
    public static final SendImgPluginCommand INSTANCE = new SendImgPluginCommand();
    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(SendImgPluginCommand.class);
    private final SendImgPluginData pluginData = SendImgPluginData.INSTANCE;

    private SendImgPluginCommand() {
        super(SendImgPlugin.INSTANCE,
                "SendImgPlugin",
                "sendimgplugin");
    }

    // 1个命令符
    @Handler
    public void onCommand(CommandSender sender, String operation) {
        operation = operation.toLowerCase(Locale.ROOT);
        switch (operation) {
            case "showtrigger":
                // 展示触发词
                if (pluginData.triggerWords.isEmpty()) {
                    logger.info("there is no content");
                }
                int idx = 1;
                for (String word : pluginData.triggerWords) {
                    logger.info("[" + idx + "] " + word);
                    idx++;
                }
                break;
            case "showimgpath":
                // 展示图片路径
                logger.info(pluginData.imgPath);
                break;
            case "showimgnum":
                // 展示图片余量
                showImgNum();
                break;
            case "showdelflag":
                // 显示是否发后删除
                logger.info(String.valueOf(pluginData.delAfterSend));
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
            case "addtrigger":
                pluginData.triggerWords.add(content);
                break;
            case "removetrigger":
                pluginData.triggerWords.remove(content);
                break;
            case "setimgpath":
                File dir = new File(content);
                if (!dir.exists() || !dir.isDirectory()) {
                    logger.error("img path is illegal");
                } else {
                    pluginData.imgPath = content;
                }
                break;
            case "setdelflag":
                if (content.toLowerCase(Locale.ROOT).equals("true")) {
                    pluginData.delAfterSend = true;
                } else if (content.toLowerCase(Locale.ROOT).equals("false")) {
                    pluginData.delAfterSend = false;
                } else {
                    logger.info("should be true or false");
                }
                break;
            case "setthreshold":
                try {
                    pluginData.threshold = Integer.parseInt(content);
                } catch (NumberFormatException e) {
                    logger.error("should be number");
                }
                break;
            default:
                logger.info("illegal operation");
        }
    }

    /**
     * 展示图片余量
     */
    private void showImgNum() {
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
}
