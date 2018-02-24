# okhttpdemo
课程名称：okhttp框架解析与应用</br>
课程地址：https://www.imooc.com/learn/732</br>
笔记：</br>
##HTTP发展及okhttp优势</br>
* HTTP出现及发展...</br>
* HTTP优势...</br>
* HTTP2.0以及SPDY介绍...</br>
* okhttp优势...</br>
* [@okhttp github地址](https://github.com/square/okhttp)</br>
* [@中文文档](http://blog.csdn.net/jackingzheng/article/details/51778793)</br>
##okhttp源码结构分析与应用</br>
* okhttp核心框架讲解</br>
okhttp基础，基本都采用了构建者模式(Builder)  
* okhttp核心类讲解</br>
Route:路由类  
ResponseBody:响应体    
Response:响应头和响应体  
RequestBody:请求体  
FormBody:继承RequestBody,只能上传key—value  
MultipartBody:可以上传对象类数据  
Request:请求头和请求体  
Headers:请求头  
Cache:缓存  
##基于okhttp封装通用网络模块</br>
注：okhttp每次用都要写很多重复代码，所以要自己封装一个工具类。  
为什么要封装通用网络模块:可复用性、与业务逻辑分离、可扩展性  
封装思路讲解：  
1.封装一个OkHttpClient  
2.封装一个通用的请求创建类CommonRequest  
3.封装一个通用的响应解析类JsonCommonRequest  
代码实战通用网络模块封装(这里我直接用的这个https://github.com/tsy12321/MyOkHttp)  
使用我们的网络模块  