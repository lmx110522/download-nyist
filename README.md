## springboot实现分享类型的项目
由于前后台都是我本人写的，加上就四五天的时间，所以，有点瑕疵！给我评论哟
**springboot特点有以下**
 1、使编码变得简单
2、使配置变得简单
3、使部署变得简单
4、使监控变得简单
由于时间比较紧张，有些功能或许不太完善，后期回去完善，谢谢您的查看!
##  里面大致功能

 1. 阿里验证码
 2. springboot框架下的RabbitMq解决同时注册削锋的问题
 3. springboot使用redis以及session实现避免多用户造成给数据库压力
 4. 网站多处使用ajax异步技术实现异步加载数据
 5. sppringdatajpa的使用，多条件共同作用下的查询结果异显示在页面
 6. 本站本来要使用socket实现管理员消息推送，但是这个功能和上个spingboot模拟QQ相似，所以就没写
 7. 希望它对您有帮助

**先上截图**
### 1 首页
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204220522671.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)
### 2 全部下载，这个有点含金量，多条件同时传递筛选，然后异步显示
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204220817594.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)
### 3 登录，使用了阿里的手机验证码登录，以及jquery实现的滑块验证
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204221552868.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)

### 4. 上传页面，实现了图片在线预览，以及自动识别格式

![在这里插入图片描述](https://img-blog.csdnimg.cn/2018120422193932.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)
### 5. 更换头像少不了的功能
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204222123649.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)

### 6.下载项详细页，里面包含评论，还有同类型精准匹配推荐
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204222313919.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)
### 7. 下载须知，这个样式有必要展示一下，挺好看的

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204222454675.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)
### 8. 管理员后台登录

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204222641692.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)
### 9. 管理员主界面，
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181204222747924.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L292ZXIxMTA1MjI=,size_16,color_FFFFFF,t_70)
### 10. 审核界面
	不一一介绍了，希望你如果真的想了解这些功能，下载下来，看看具体功能实现步骤，上面的网站照片主要是让你了解一下网站的大致功能
	以及如何显示，具体代码请查看github网站，如果喜欢，给个小星星 ！


>github地址： https://github.com/lmx110522/download-nyist.git   欢迎访问我的[个人博客](https://lmx110522.github.io/)。
