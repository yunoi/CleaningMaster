package com.example.yunoi.cleaningmaster;

public class TodayListVO {

    private String task;
    private int checkCount;
    private String area;

    public TodayListVO(String task, int checkCount,String area) {
        this.task = task;
        this.checkCount = checkCount;
        this.area = area;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
