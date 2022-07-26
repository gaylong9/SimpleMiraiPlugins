# RemindMoYuPlugin

定时在群内发送消息，以提醒群员摸鱼（提肛、喝水、上厕所等）。

通过设定时间、设定群聊、设定语料，bot会每隔24h，在设定的时间，向所有设定的群组，从所有语料中随机选择一句发送。

yml数据存在

`mirai-console/data/org.gaylong9.RemindMoYuPlugin/RemindMoYuPluginData.yml`。



## Usage

下载[`RemindMoYuPlugin.jar`](https://github.com/gaylong9/SimpleMiraiPlugins/releases/tag/jar)放在`mirai-console/plugins`下，启动mcl。

指令 `/RemindMoYuPlugin <Operation> [content]`，支持全小写`/remindmoyuplugin <operation> [content]`。

使用`addGroup`命令添加群组；

使用`addMsg`命令添加语料：
* QQ表情：`[face:QQ表情编号]`或`[face:QQ表情中文名]`，如`[face:320]` `[face:庆祝]`，表示表情——庆祝 [Mirai中Face编号与名字查看](https://github.com/mamoe/mirai/blob/dev/mirai-core-api/src/commonMain/kotlin/message/data/Face.kt)

* 换行：输入`\\n`

* eg：`该摸鱼啦！\\n[face:庆祝]` 

	![example](README/example.png)

使用`addTime`命令添加发送消息时间，应为`HH:mm`格式。

在mcl中输入`?`查看全部命令。

## Todo

- [ ] QQ中执行命令

- [ ] 权限功能

