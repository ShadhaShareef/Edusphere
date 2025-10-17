package com.edusphere.models;

public class Result {
    private String username;
    private int quizId;
    private int score;
    private int total;

    public Result() {}

    public Result(String username, int quizId, int score, int total) {
        this.username = username;
        this.quizId = quizId;
        this.score = score;
        this.total = total;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
}
