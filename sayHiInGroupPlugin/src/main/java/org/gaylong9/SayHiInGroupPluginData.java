package org.gaylong9;

import java.util.*;

public class SayHiInGroupPluginData {

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

    public static final SayHiInGroupPluginData INSTANCE = new SayHiInGroupPluginData();
    private final String saveName;
    public SayHiInGroupPluginData() {
        saveName = "SayHiInGroupPluginData";
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

}