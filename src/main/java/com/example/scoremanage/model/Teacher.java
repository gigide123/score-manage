package com.example.scoremanage.model;

public class Teacher {
    private String tid;
    private String name;
    private String subject;
    private int class_;

    // 无参构造
    public Teacher() {}

    // 有参构造
    public Teacher(String tid, String name) {
        this.tid = tid;
        this.name = name;
        this.subject = "";
        this.class_ = 0;
    }

    // Getter + Setter
    public String getTid() { return tid; }
    public void setTid(String tid) { this.tid = tid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public int getClass_() { return class_; }
    public void setClass_(int class_) { this.class_ = class_; }
}