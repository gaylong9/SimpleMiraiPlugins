package org.gaylong9;

import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.ArrayList;
import java.util.List;

public class RepeatPluginData {
    public static final RepeatPluginData INSTANCE = new RepeatPluginData();
    private final String saveName;
    public RepeatPluginData() {
        saveName = "RepeatPluginData";
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.add("");
        emptyMsg = lastReceive = lastSend = builder.build();
    }

    public String getSaveName() {
        return saveName;
    }

    // 插件是否运行中，默认运行
    public Boolean isRunning = true;
    // 群聊模式，全体群聊都生效or指定群聊生效，默认全体生效
    public String mode = "all";
    // 添加群聊号码，指定群聊mode下仅在添加过的群聊中生效,无默认值, 自动创建空
    public List<Long> groups = new ArrayList<>();
    // 上一条接收到的消息

    public MessageChain lastReceive;
    // 上一条发送的消息
    public MessageChain lastSend;
    public MessageChain emptyMsg;

}