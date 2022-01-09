# RepeatPlugin

复读bot

![exampleImg](README/exampleImg.png)

yml数据存在

`mirai-console/data/org.gaylong9.RepeatPlugin/RepeatPlugin.yml`。



## Usage

下载[`RepeatPlugin.jar`](https://github.com/gaylong9/SimpleMiraiPlugins/releases/tag/jar)放在`mirai-console/plugins`下，启动mcl。

指令 `/RepeatPlugin <operation> (content)`：

* `/RepeatPlugin start` 启动插件（默认启动）
* `/RepeatPlugin stop` 关闭插件
* `/RepeatPlugin switchGroupMode all/specific` 
	* `all`：bot所在的所有群聊都生效（默认）
	* `specific`：指定群聊生效
* `/RepeatPlugin showGroupMode` 显示当前模式
* `/RepeatPlugin showGroup` 显示specific模式下哪些群聊可以生效
* `/RepeatPlugin addGroup <groupId>` 添加群号，用于specific模式
* `/RepeatPlugin removeGroup <groupId> ` 删除群号，用于specific模式
* `/RepeatPlugin containGroup <groupId> ` 检查群号是否生效，用于specific模式
* 指令中字母全小写同样可以，如`/repeatplugin switchgroupmode specific` 

