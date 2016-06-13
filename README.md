# AIDLTest
AIDL
#简单调用adil的demo介绍
#步骤：1、写aidl文件，实现序列化对象的参数需要显式的import进去aidl文件中
#2、序列化的对象和aidl文件服务端和客户端需要路径一致
#3、在ServiceConnection中获取实现aidl的子类的对象，就可以调用其方法了
#4、死亡代理，可以监听服务是否已经被kill掉，与onServiceDisconnected不一样的就是所在的线程不一样
#5、bindService的时候需要指定Component，不然会报service intemt must be explicit
