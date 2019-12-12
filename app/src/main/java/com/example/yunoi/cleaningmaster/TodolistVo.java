package com.example.yunoi.cleaningmaster;

public class TodolistVo {

    private int year;
    private int month;
    private int day;
    private String groupName;
    private String todolist_text;
    private int taskcount;
    private int checkcount;

    public TodolistVo() {
    }

    public TodolistVo(int year, int month, int day, String groupName, String todolist_text, int taskcount, int checkcount) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.groupName = groupName;
        this.todolist_text = todolist_text;
        this.taskcount = taskcount;
        this.checkcount = checkcount;
    }

    public TodolistVo(String groupName, String todolist_text) {
        this.groupName = groupName;
        this.todolist_text = todolist_text;
    }

    public TodolistVo(String groupName) {
        this.groupName = groupName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(int taskcount) {
        this.taskcount = taskcount;
    }

    public int getCheckcount() {
        return checkcount;
    }

    public void setCheckcount(int checkcount) {
        this.checkcount = checkcount;
    }

    public String getTodolist_text() {
        return todolist_text;
    }

    public void setTodolist_text(String todolist_text) {
        this.todolist_text = todolist_text;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
