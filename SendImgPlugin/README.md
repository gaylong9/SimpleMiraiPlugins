# SendImgPlugin

根据设定的触发词，在群里发送指定路径下的数张图片。

yml数据存在

`mirai-console/data/org.gaylong9.SendImgPlugin/SendImgPluginData.yml`。


## Usage

下载[`SendImgPlugin.jar`](https://github.com/gaylong9/SimpleMiraiPlugins/releases/tag/jar)放在`mirai-console/plugins`下，启动mcl。

指令 `/SendImgPlugin <Operation> [content]`，支持全小写`/sendimgplugin <operation> [content]`）。

使用命令`setTrigger`设置触发词;

使用命令`setImgPath`设置图片目录；

使用命令`setDelFlag`设置发送图片后是否删除本地文件；

使用命令`setThreshold`设置一次发送最多几张图片，实际发送数量为[1, threshold]中的随机数；

QQ群内发送触发词即可。

在mcl中输入`?`查看全部命令。
