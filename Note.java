package com.edusphere.models;

public class Note {
    private String id;
    private String title;
    private String description;
    private String subject;
    private String filePath;
    private String fileHash;  // for duplicate detection

    public Note(String id, String title, String description, String subject, String filePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.filePath = filePath;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubject() { return subject; }
    public String getFilePath() { return filePath; }
    public String getFileHash() { return fileHash; }

    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
}
