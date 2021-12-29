package org.example;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.concurrent.ThreadLocalRandom;

public final class SayHiInGroupplugin extends JavaPlugin {
    public static final SayHiInGroupplugin INSTANCE = new SayHiInGroupplugin();

    private SayHiInGroupplugin() {
        super(new JvmPluginDescriptionBuilder("org.example.sayHiInGroupplugin", "1.0-SNAPSHOT")
                .name("SayHiInGroup")
                .author("jsy")
                .build());
    }

    @Override
    public void onEnable() {

        MiraiLogger logger = getLogger();
        logger.info("say Hi In Group Plugin loaded!");

        Listener<GroupMessageEvent> listener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, (GroupMessageEvent event) -> {
            Member sender = event.getSender();
            String content = event.getMessage().serializeToMiraiCode();
            // 手机QQ艾特会在尾部多一个空格
            if (content.trim().equals("[mirai:at:2012391808]")) {
                PlainText text = new PlainText("你好啊，");
                String name = sender.getNameCard();
                if (name.isEmpty()) {
                    name = sender.getNick();
                }
                ThreadLocalRandom random = ThreadLocalRandom.current();
                int faceId = random.nextInt(0, 325);
                while (Face.names[faceId] == null || Face.names[faceId].isEmpty()) {
                    faceId = random.nextInt(0, 325);
                }
                Face face = new Face(faceId);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(text);
                builder.add(name);
                builder.add(face);
                event.getSubject().sendMessage(builder.build());
            }
        });
    }
}