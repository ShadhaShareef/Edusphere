package com.edusphere.controllers;

import io.javalin.Javalin;
import com.edusphere.models.Note;
import com.edusphere.utils.FileUtils;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class NoteController {
    private static final String DATA_FILE = "src/main/resources/data/notes.json";

    public static void registerRoutes(Javalin app) {

        // Upload a new note
        app.post("/upload", ctx -> {
            String title = ctx.formParam("title");
            String description = ctx.formParam("description");
            String subject = ctx.formParam("subject");

            var uploadedFile = ctx.uploadedFile("file");
            if (uploadedFile == null) {
                ctx.status(400).json(Map.of("error", "No file uploaded"));
                return;
            }

            // Temporary Note object
            Note newNote = new Note(UUID.randomUUID().toString(), title, description, subject, "");

            // Save temporarily to compute hash
            Files.createDirectories(Paths.get("uploads/"));
            String tempPath = "uploads/temp_" + uploadedFile.filename();
            try (InputStream input = uploadedFile.content();
                 OutputStream output = new FileOutputStream(tempPath)) {
                input.transferTo(output);
            }

            // Compute MD5 hash
            try {
                String hash = FileUtils.getFileHash(Path.of(tempPath));
                newNote.setFileHash(hash);
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Error computing file hash"));
                Files.deleteIfExists(Path.of(tempPath));
                return;
            }

            // Check duplicates
            if (FileUtils.isDuplicate(newNote)) {
                ctx.status(400).json(Map.of("error", "A note with the same title/subject or identical file already exists."));
                Files.deleteIfExists(Path.of(tempPath));
                return;
            }

            // Final file path with UUID to avoid collisions
            String finalFileName = UUID.randomUUID() + "_" + uploadedFile.filename();
            String finalPath = "uploads/" + finalFileName;
            Files.move(Path.of(tempPath), Path.of(finalPath), StandardCopyOption.REPLACE_EXISTING);
            newNote.setFilePath("/uploads/" + finalFileName);

            // Save to JSON
            List<Note> notes = FileUtils.loadNotes();
            notes.add(newNote);
            FileUtils.saveListToJson(DATA_FILE, notes);

            ctx.json(Map.of("message", "Note uploaded successfully"));
        });

        // Fetch all notes
        app.get("/notes", ctx -> {
            List<Note> notes = FileUtils.loadListFromJson(DATA_FILE, new TypeToken<List<Note>>() {});
            ctx.json(notes);
        });
    }
}
