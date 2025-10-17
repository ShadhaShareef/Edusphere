package com.edusphere.models;

import java.util.List;

public class Quiz {
    private int id;
    private String title;
    private String subject;       // <-- add this
    private List<Question> questions;

    public Quiz() {}

    public Quiz(int id, String title, String subject, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.subject = subject;   // <-- initialize
        this.questions = questions;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSubject() { return subject; }   // <-- add getter
    public List<Question> getQuestions() { return questions; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setSubject(String subject) { this.subject = subject; }  // <-- add setter
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}