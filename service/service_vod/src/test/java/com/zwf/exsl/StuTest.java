package com.zwf.exsl;

import com.alibaba.excel.EasyExcel;
import com.zwf.exsl.Stu;

import java.util.ArrayList;
import java.util.List;

public class StuTest {


    private static List<Stu> data() {
        List<Stu> list = new ArrayList<Stu>();
        for (int i = 0; i < 10; i++) {
            Stu data = new Stu();
            data.setSno(i);
            data.setSname("张三" + i);
            list.add(data);
        }
        return list;
    }

    public static void main(String[] args) {
        String firename="E:\\11.xlsx";
        EasyExcel.write(firename,Stu.class).sheet("写操作").doWrite(data());
    }





}
