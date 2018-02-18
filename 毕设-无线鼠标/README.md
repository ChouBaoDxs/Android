# 无线鼠标APP
项目起因：毕业设计</br>
简要说明：只有演示，代码不便放出</br>
基本原理：使用Java编写一个运行在PC端上的Server，与本APP进行无线局域网内Socket通信</br>
基本功能：</br>
* 鼠标光标移动</br>
* 双指滚动</br>
* 普通键盘(支持各种组合按键)</br>
* 触控板</br>
* 换壁纸(纯色或相册图片)</br>
另外说明：那个悬浮按钮的作用是按住后可以模拟光电鼠标效果，原理是通过手机的线性加速度传感器获取x，y两个方向的加速度，根据位移计算公式转换成光标移动信号，误差较大，使用体验非常差</br>

大致效果（video大小为9.1MB）：</br>
<video id="video" controls="" preload="none">
    <source id="mp4" src="https://github.com/ChouBaoDxs/MyResources/blob/master/video/showWiFiMouse.mp4" type="video/mp4">
</video>
