package com.edusphere.models;

public class Scholarship {
    private int id;
    private String title;
    private String about;
    private String awardValue;
    private String eligibility;
    private String deadlineDate;
    private String applyLink;

    public Scholarship() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public String getAwardValue() { return awardValue; }
    public void setAwardValue(String awardValue) { this.awardValue = awardValue; }

    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }

    public String getDeadlineDate() { return deadlineDate; }
    public void setDeadlineDate(String deadlineDate) { this.deadlineDate = deadlineDate; }

    public String getApplyLink() { return applyLink; }
    public void setApplyLink(String applyLink) { this.applyLink = applyLink; }
}
