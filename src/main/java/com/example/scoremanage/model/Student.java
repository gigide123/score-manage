package com.example.scoremanage.model;

public class Student {
    private String sid;
    private String name;
    private int chinese;
    private int math;
    private int english;
    private int allScore;
    private int class_;

    // 无参构造（MyBatis 必须）
    public Student() {}

    // 有参构造（对应 Python __init__）
    public Student(String sid, String name) {
        this.sid = sid;
        this.name = name;
        this.chinese = 0;
        this.math = 0;
        this.english = 0;
        this.allScore = 0;
        this.class_ = 0;
    }

    // Getter + Setter（Java 必须）
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
    public int getAllScore() { return allScore; }
    public void setAllScore(int allScore) { this.allScore = allScore; }
    public int getClass_() { return class_; }
    public void setClass_(int class_) { this.class_ = class_; }
}