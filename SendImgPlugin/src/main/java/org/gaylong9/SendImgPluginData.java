package org.gaylong9;

import net.mamoe.mirai.utils.MiraiLogger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.time.LocalTime;
import java.util.*;

public class SendImgPluginData {
    static final SendImgPluginData INSTANCE = new SendImgPluginData();
    private final String saveName = "SendImgPluginData";
    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(SendImgPluginData.class);

    private SendImgPluginData() {}

    String getSaveName() {
        return saveName;
    }

    // 触发词
    HashSet<String> triggerWords = new HashSet<>();

    // 图片路径
    String imgPath = "";

    // 发送后是否删除
    boolean delAfterSend = false;

    // 发送图片数量上限
    int threshold = 1;
}
