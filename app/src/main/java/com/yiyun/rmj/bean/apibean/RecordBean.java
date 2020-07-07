package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class RecordBean {
    private int state;
    private Info info;

    public class Info{
        private int isNull;
        private String message;
        ArrayList<Record> data;

    }

    public class Record{
    }

}
