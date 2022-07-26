package org.gaylong9;

import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepeatPluginData {

    enum MODE {
        /**
         * 对所有群组生效
         */
        ALL("ALL"),
        /**
         * 仅对指定的群组生效
         */
        SPECIFIC("SPECIFIC");

        private String name;

        private MODE(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static final RepeatPluginData INSTANCE = new RepeatPluginData();
    private final String saveName;
    private RepeatPluginData() {
        saveName = "RepeatPluginData";
        MessageChainBuilder builder = new MessageChainBuilder();
        builder.add("");
        emptyMsg = builder.build();
    }

    public String getSaveName() {
        return saveName;
    }

    // 插件是否运行中，默认运行
    public Boolean isRunning = true;
    // 群聊模式，全体群聊都生效or指定群聊生效，默认全体生效
    public MODE mode = MODE.ALL;
    // 添加群聊号码，指定群聊mode下仅在添加过的群聊中生效,无默认值, 自动创建空
    public List<Long> groups = new ArrayList<>();


    // 上一条接收到的消息，运行时数据，不保存进yaml
    public HashMap<Long, MessageChain> lastReceive = new HashMap<>();
    // 上一条发送的消息，运行时数据，不保存进yaml
    public HashMap<Long, MessageChain> lastSend = new HashMap<>();

    // 辅助
    public MessageChain emptyMsg;

}