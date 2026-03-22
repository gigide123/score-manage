package com.example.scoremanage.model;

public class StudentData {
    private String sid;
    private String name;
    private int chinese;
    private int math;
    private int english;
    private int class_;

    // Getter + Setter
    public String getSid() { return sid; }
    public void setSid(String sid) { this.sid = sid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getChinese() { return chinese; }
    public void setChinese(int chinese) { this.chinese = chinese; }
    public int getMath() { return math; }
    public void setMath(int math) { this.math = math; }
    public int getEnglish() { return english; }
    public void setEnglish(int english) { this.english = english; }
    public int getClass_() { return class_; }
    public void setClass_(int class_) { this.class_ = class_; }
}