package com.example.aidlservice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenxiaojiong on 2016/6/12.
 */
public class Person implements Parcelable {

    private String name;

    public Person(String name){

        this.name=name;
    }

    protected Person(Parcel in) {
        name=in.readString();
    }
    /**
     * 从序列化的对象中创建原始的对象
     */
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    //返回当前对象的描述
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * 实现对象的序列化
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
