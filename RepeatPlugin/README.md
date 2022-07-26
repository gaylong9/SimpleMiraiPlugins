# RepeatPlugin

复读bot

![exampleImg](README/exampleImg.png)

yml数据存在

`mirai-console/data/org.gaylong9.RepeatPlugin/RepeatPlugin.yml`。



## Usage

下载[`RepeatPlugin.jar`](https://github.com/gaylong9/SimpleMiraiPlugins/releases/tag/jar)放在`mirai-console/plugins`下，启动mcl。

指令 `/RepeatPlugin <Operation> [content]`；支持全小写`/repeatplugin <operation> [content]`。

本插件有“群组模式”设置项，为ALL时，表示对Bot所在的所有群聊生效；为SPECIFIC时，仅对指定的群组生效。

可使用`showGroupMode`命令查看当前群组模式；使用`switchGroupMode`命令切换；使用`addGroup`命令添加生效群组。

在mcl中输入`?`查看全部命令。