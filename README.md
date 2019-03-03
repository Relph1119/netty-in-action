# 《Netty实战》阅读笔记
&emsp;&emsp;本书中介绍的Netty是一款用于快速开发高性能的网络应用程序的Java框架，有较高的Java网络编程API的封装性。  
&emsp;&emsp;个人感觉Netty是一个管道过滤器的架构模式，采用Channel->ChannelCodec->ChannelHandler，整套封装在ChannelPipeline中，最后通过Bootstrap执行。  
&emsp;&emsp;本书的正文和代码注释，翻译比较生硬，建议需要采用SpringWebflux的同学最好看本书的英文原版，笔者为理解该书中内容，对照着原版阅读。
  
**以下是英文原版代码的github中的REMADE.md**  
This Repository contains the source-code for all chapters of the book [Netty in Action](http://manning.com/maurer)
by Norman Maurer and Marvin Allen Wolfthal.

Latest version: https://github.com/normanmaurer/netty-in-action/tree/2.0-SNAPSHOT

Enjoy! Feedback and PR's welcome!


Prerequisites

	JDK 1.7.0u71 or better

	Maven 3.3.9 or better


If you want to build everything at once, from the top directory run

	mvn install


If you want to build only single projects then from the top directory first run

	mvn install -pl utils


This will make the utils jar available to all the projects.
