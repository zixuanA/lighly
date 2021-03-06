# lighly
一个轻巧的音乐软件
# 接口没了—_—
![image](https://github.com/zixuanA/lighly/blob/master/Screenshot_1566653940.png)
![image](https://github.com/zixuanA/lighly/blob/master/Screenshot_1566654040.png)
[演示视频](https://github.com/zixuanA/lighly/blob/master/%E5%B1%8F%E5%B9%95%E5%BD%95%E5%88%B6%202019-08-24%20%E4%B8%8B%E5%8D%8811.50.04.mov)
## 基于MVVM框架开发
  不同模块解耦
  
## 一个动画（踩坑）集合
  * 使用了多种不同的动画，ValueAnimator,ObjectAnimator以及Transition等安卓动画类,实现了多种不同的动画效果
  * 在recyclerview中通过属性动画绘制了弹跳的小方块
  * 首页到歌单页的扩散的转场动画
  * 歌单页的收缩动画
  * 搜索页的入场动画
  * 歌单页到歌曲页的共享元素转场动画
  * 歌曲页的小部件点击动画
  * 自定义view中封装的波纹动画

## 使用EventBus作为事件总线，可以在多个页面展示相同的歌曲及进度
*  它不会随着需要展示音乐播放进度的页面的怎加而变得过度复杂，增加新的进度展示页面也只需要注册EventBus同时监听音乐状态事件即可。

## 多个音乐收藏夹
*  类似于网易云音乐的收藏系统，可以直接收藏到我喜欢的音乐，也可自建多个音乐收藏夹
  
## 动态下发的关键词清单 收藏页的云端存储
*  通过leancloud的数据存储功能实现对音乐关键词的动态下发，及云端存储个人数据

## 多个不同的自定义view
*   监听了view的TouchEvent及计算实现了拖动的功能
   
# 缺陷
* 歌曲收藏夹暂时不支持删除
* 关键词栏在滑动快的时候会出现多个同时展开的情况

## 踩坑
### leancloud 文档坑
* 部分文档不完整，导致在读取云端文件时做了很多测试，踩坑
* 下次一定学一些后端

### 属性动画在RecyclerView的item中使用 存在item的刷新及在滑动太快时存在属性动画未执行的情况
* 部分解决：通过反射降低recyclerview的最大滑动速度，改进弹跳的算法，降低同时多个子项展开的概率

### Transition类在recyclerview子项滑动的情况下会出现异常导致退出的情况
* 解决：给动画设置极短的持续事件，解决异常

### Transition类部分情况设置无效
* Transition延迟动画必须设置在改变属性之前，并且不能立即将属性还原（将导致动画不执行

### 未找到解决方案的Transition类问题
* 在转场动画时移除目标view失败
* 在共享元素的转场动画后该元素进行其他的动画，有一定概率会导致该元素在返回动画时不执行
* 同时国内ttransition类的资料相对较少，使用示例也较少，耗费了较多时间

### 做项目做到一半原来使用的接口出问题不反回数据
换用了其他接口

### 在找接口的时候找到的接口只有一个搜索功能
在app制作过程中对于不同的功能实现的少，以视觉效果与动画为主

### 使用了掌上重邮的开发框架，在最后移植代码时出现了一些问题
* 导致正式版本也还带有新生专题的包名

### leancloud与安卓9.0配置支持http相冲突，会导致奇怪的报错
* 将http的图片接口手动转换为https

### 数学问题
* 自定义控件时计算了很久的拖动事件，然而在接近一百八十度时依旧会出现问题，改动了拖动的角度
下次一定好好学数学

## 提高
* 在这次项目中使用了很多的动画 ，在这个暑假前还从未使用过
* 自定义view也是在暑假中才第一次实战使用
* mvvm框架使用提高，较第一次使用时对mvvm框架的控制更好了一些，同时没有再使用databinding这个天坑
* 实现了在迎新专题中没能实现的recyclerview中的动画，对recyclerview进行了一些魔改，但还是存在一些问题，只能通过源码了解
* 加班熬夜✖️ 健康养生✔️
* 这次的软件也是因为看到gif图觉得很好看想自己动手制作的，大体上还原了gif上的内容，有一部分由于接口的限制没能做出来，
准备之后学习部分后端的知识，手动搭一个音乐播放的后端，同时想要接入apple music的内容，希望这时再次对这个app进行重构
* 这是写出来的第一个可以使用的音乐软件，相较于xx云音乐更干净，同时曲库也较大，但由于本身页面的篇幅限制，每个歌单页加载的音乐并不多
同时还存在容易被系统杀后台的情况
* 在网上寻找可以使用的接口的时候才发现了数据的重要性，没有数据，最先找到的接口又只有一个搜索功能，直接导致的对搜索找音乐的不便
* 同时试用了lottie框架，但是由于资源文件与项目的不太搭，而放弃（论视觉小姐姐的重要性）
* 为这一次项目9107工作制，在网校上班了十多天（不想享受996的福报）
* 有一些问题目前还解决不掉，希望之后有机会研究源码找到解决的方法
* 同时目前对编程的学习也多局限于安卓框架内页面的实现，对实现原理以及计算机的通用知识，以及数学英语的学习还不太够（下次一定好好学）

