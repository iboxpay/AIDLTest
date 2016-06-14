package com.example.chenxiaojiong.myaidltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.aidlservice.IPersonManager;
import com.example.aidlservice.Person;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private IPersonManager mPersonManager;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent = new Intent();
        //5.0之后必须显式声明(单单设置这个的话，会报service intent must be explicit)
//        mIntent.setPackage(getPackageName());
        //设置action
        mIntent.setAction("ggg");
        //指的你要调的Service（设置了这个的话就不会报上述异常，但是如果你单独设置了package，没有设置Component的话，什么鸟都不会报
        // 但是没有aidl没有效果）
        mIntent.setComponent(new ComponentName("com.example.aidlservice",
                "com.example.aidlservice.PersonManagerService"));
        bindService(mIntent,mServiceConnection,BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("AIDL","服务起飞了");
            //service就是AIDL返回的Binder对象
            mPersonManager = IPersonManager.Stub.asInterface(service);
            try {
                //注册一个IBinder.DeathRecipient类型的对象
                //  当这个IBinder所对应的Service进程被异常的退出时，比如被kill掉，这时系统会调用这个IBinder
                // 之前通过linkToDeath注册的DeathRecipient类对象的binderDied函数。
                //一般实现中，Bp端会注册linkToDeath，目的是为了监听绑定的Service的异常退出，
                // 一般的binderDied函数的实现是用来释放一些相关的资源。
                mPersonManager.asBinder().linkToDeath(mDeathRecipient, 0);
                //使用服务端的对象，获取服务中存储的person
                ArrayList<Person> personList =(ArrayList<Person>) mPersonManager.getPersonList();
                Log.e("AIDL",personList.toString()+";数目:"+personList.size());
                //使用服务端的对象，添加name
                mPersonManager.addPerson(new Person("005"));
                ArrayList<Person> personList2 =(ArrayList<Person>) mPersonManager.getPersonList();
                Log.e("添加后的AIDL",personList2.toString()+";数目:"+personList2.size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPersonManager=null;
            Log.e("AIDL","Binder died："+Thread.currentThread().getName());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    /**
     * 服务的死亡代理
     */
    private IBinder.DeathRecipient mDeathRecipient=new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(mPersonManager==null){
                return;
            }
//            取消接收Binder结点的死亡通知
            mPersonManager.asBinder().unlinkToDeath(mDeathRecipient,0);
            mPersonManager=null;
            //这里进行重新绑定远程服务(运行在Binder的线程池中的，不能直接访问UI)
            Log.e("AIDL", "服務需要重新起飛:"+Thread.currentThread().getName());
            bindService(mIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    };
}
