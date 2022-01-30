# SendImgPlugin

根据设定的触发词，在群里发送指定路径下的一张图片。

yml数据存在

`mirai-console/data/org.gaylong9.SendImgPlugin/SendImgPluginData.yml`。



## Usage

下载[`SendImgPlugin.jar`](https://github.com/gaylong9/SimpleMiraiPlugins/releases/tag/jar)放在`mirai-console/plugins`下，启动mcl。

设置触发词 - 设置图片目录 - QQ群内发送触发词即可。

指令 `/SendImgPlugin <operation> <content>`：（指令中字母全小写同样可以，如`/sendimgplugin showtrigger`）

* `/SendImgPlugin showTrigger` 显示触发词
* `/SendImgPlugin addTrigger <content>` 添加触发词，如`/SendImgPlugin addTrigger 来只猫猫`  
* `/SendImgPlugin remvoeTrigger <content> ` 删除触发词
* `/SendImgPlugin showImgPath` 显示当前设置的图片目录
* `/SendImgPlugin setImgPath <path>` 设置图片目录，如`/SendImgPlugin setImgPath d:/temp` 
* `/SendImgPlugin showImgNum` 显示当前图片余量（指定目录下文件数）
* `/SendImgPlugin showDelFlag` 显示是否发送后删除图片（默认false）
* `/SendImgPlugun setDelFlag <true or false>` 设置发送后是否删除，仅支持`true`或`false` 

