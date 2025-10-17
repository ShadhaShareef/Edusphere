package com.edusphere.models;

import java.util.List;

public class Question {
    private String q;
    private List<String> options;
    private String answer;

    public Question() {}

    public Question(String q, List<String> options, String answer) {
        this.q = q;
        this.options = options;
        this.answer = answer;
    }

    public String getQ() { return q; }
    public List<String> getOptions() { return options; }
    public String getAnswer() { return answer; }

    public void setQ(String q) { this.q = q; }
    public void setOptions(List<String> options) { this.options = options; }
    public void setAnswer(String answer) { this.answer = answer; }
}
