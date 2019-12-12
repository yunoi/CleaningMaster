package com.example.yunoi.cleaningmaster;

public class TodolistVo {

    private String todolist_text;

    public TodolistVo(String todolist_text) {
        this.todolist_text = todolist_text;
    }


    public String getTodolist_text() {
        return todolist_text;
    }

    public void setTodolist_text(String todolist_text) {
        this.todolist_text = todolist_text;
    }
}
