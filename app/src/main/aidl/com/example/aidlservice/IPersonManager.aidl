// IBookManager.aidl
package com.example.aidlservice;

// Declare any non-default types here with import statements

//自定义的parcelable对象必须显式的import进来
import com.example.aidlservice.Person;

interface IPersonManager {

    List<Person> getPersonList();
    //除了基本类型之外，其他类型必须标上方向
    void addPerson(in Person person);
}
