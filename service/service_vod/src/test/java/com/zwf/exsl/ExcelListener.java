package com.zwf.exsl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener extends AnalysisEventListener<Stu> {
    List<Stu> list = new ArrayList<Stu>();
    @Override
    public void invoke(Stu stu, AnalysisContext analysisContext) {
        System.out.println(stu);
        list.add(stu);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
