package org.gaylong9;

import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.MiraiLogger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class RemindMoYuPluginData {
    static final RemindMoYuPluginData INSTANCE = new RemindMoYuPluginData();
    private final String saveName;
    private final MiraiLogger logger = MiraiLogger.Factory.INSTANCE.create(RemindMoYuPluginData.class);

    private RemindMoYuPluginData() {
        this.saveName = "RemindMoYuPluginData";
        for (int i = 0; i < Face.names.length; i++) {
            if (Face.names[i] == null || Face.names[i].equals("表情")) {
                continue;
            }
            faceId.put(Face.names[i].substring(1, Face.names[i].length() - 1), i);
        }
    }

    String getSaveName() {
        return saveName;
    }

    // 插件是否运行中，默认运行
    Boolean isRunning = true;

    // 添加群聊号码，仅支持添加群聊中提醒摸鱼，无默认值, 自动创建空
    List<Long> groups = new ArrayList<>();

    // 提醒语料，"[face:QQ表情编号]"或"[face:QQ表情名]"将解析为QQ表情，如"[face:320]"、"[face:庆祝]"；
    // 换行需要输入"\\n"
    // eg："该摸鱼啦！\\n[face:320]"
    // QQ表情查阅：https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/Face.kt
    List<String> contents = new ArrayList<>();

    // 提醒时间 HH:mm，到达提醒时间时从语料中随机选择一句 发送到所有指定群聊中
    List<String> times = new ArrayList<>();

    // MessageChain，由contents解析得到的运行时消息数据，不存进yaml
    List<MessageChain> messageChains = new ArrayList<>();

    // LocalTime，运行时数据，不存进yaml
    List<LocalTime> localTimes = new ArrayList<>();

    // 线程池，运行时数据，不存进yaml
    ScheduledThreadPoolExecutor executor;

    // 用于取消任务，运行时数据，不存进yaml
    List<RunnableScheduledFuture<?>> tasks = new ArrayList<>();

    // 用于face生成，运行时数据，不存进yaml
    Map<String, Integer> faceId = new HashMap<>();

    // 标记任务是否加载过，若掉线后重登陆不再重新加载，运行时数据，不存进yaml
    // 若重新登陆后旧任务因bot原因无法执行，则需改为登录后清空executor和tasks重新load
    boolean hasLoadedTasks = false;

}
