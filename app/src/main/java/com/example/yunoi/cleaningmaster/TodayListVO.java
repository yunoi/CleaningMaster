package com.example.yunoi.cleaningmaster;

public class TodayListVO {

    private String task;
    private int checkCount;

    public TodayListVO(String task, int checkCount) {
        this.task = task;
        this.checkCount = checkCount;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }
}
