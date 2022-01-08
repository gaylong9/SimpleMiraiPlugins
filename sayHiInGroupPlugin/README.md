# SayHiInGroup

在群聊中被艾特时回复`你好啊, [群名片][随机表情]`。

![exampleImg](README/exampleImg.png)



因`JAutoSavePluginData`有bug，转为自行实现数据持久化。数据文件同原生`PluginData`一致，存在

`mirai-console/data/org.gaylong9.SayHiInGroupPlugin/PluginData.yml`。



## Todo

- [x] cmd名称统一
- [x] cmd增加小写次名
- [x] 增加删除group的cmd
- [x] cmd规范log
- [x] 自动保存，数据持久化到yml（Java的JAutoSavePluginData有bug，当前只好混用kotlin/自己实现持久化，使用了YAML与SnakeYAML）



## Usage

下载`SayHiInGroupPlugin.jar`放在`mirai-console/plugins`下，启动mcl。

指令 `/sayHiInGroupPlugin <operation> (content)`：

* `/sayHiInGroupPlugin start` 启动插件（默认启动）
* `/sayHiInGroupPlugin stop` 关闭插件
* `/sayHiInGroupPlugin switchGroupMode all/specific` 
	* `all`：bot所在的所有群聊都生效（默认）
	* `specific`：指定群聊生效
* `/sayHiInGroupPlugin showGroupMode` 
* `/sayHiInGroupPlugin showGroup` 显示specific模式下哪些群聊可以生效
* `/sayHiInGroupPlugin addGroup <groupId>` 添加群号，用于specific模式
* `/sayHiInGroupPlugin removeGroup <groupId> ` 删除群号，用于specific模式
* `/sayHiInGroupPlugin containGroup <groupId> ` 检查群号是否生效，用于specific模式
* 指令中字母全小写同样可以，如`/sayhiingroupplugin switchgroupmode specific` 

