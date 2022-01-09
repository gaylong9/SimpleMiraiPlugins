# SimpleMiraiPlugins
简简单单做着玩的Mirai机器人插件（**Java版本**）

Mirai配置参考：

[Mirai开发文档](https://docs.mirai.mamoe.net/)

[Java版本编写起步指南](https://blog.csdn.net/Ghasta/article/details/112974779) （Mirai已支持IDEA插件，一键生成插件项目）



### 当前内容

* [sayHiInGroupPlugin](https://github.com/gaylong9/SimpleMiraiPlugins/tree/main/sayHiInGroupPlugin)：试验demo，群内艾特bot会回复
* [RemindMoYuPlugin](https://github.com/gaylong9/SimpleMiraiPlugins/tree/main/RemindMoYuPlugin)：定时发送提醒摸鱼的消息
* [RepeatPlugin](https://github.com/gaylong9/SimpleMiraiPlugins/tree/main/RepeatPlugin)：复读bot



### TODO

- [x] 学习添加bot配置参数、数据持久化
- [x] 复读bot
- [ ] RemindMoYuPlugin添加命令功能，并丰富提醒语句库



## 注意

* 开发插件时，多个插件内的类名（如命令类`Command`、数据类`PluginData`等）不能相同，否则放在一个console中运行时会出错



[其他作者的好玩功能](https://github.com/Genanik/Mirai-picfinder-robot)
