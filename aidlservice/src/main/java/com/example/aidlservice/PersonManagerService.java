package com.example.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiaojiong on 2016/6/13.
 * 服务就需要先注册，避免忘记
 */
public class PersonManagerService extends Service {
    //建立一个数组用来存储person
    private ArrayList<Person> mArrayList = new ArrayList<Person>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service", "onBind");
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i("service",e.toString());
                }
                //结束自己
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).start();
        //需要返回AIDL的Binder对象
        return mBinder;
    }

    private Binder mBinder = new IPersonManager.Stub(){

        @Override
        public List<Person> getPersonList() throws RemoteException {
            //直接返回服务端的person数组
            return mArrayList;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {
            mArrayList.add(person);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Person person = new Person("007");
        Person person1 = new Person("008");
        mArrayList.add(person);
        mArrayList.add(person1);
    }
}
