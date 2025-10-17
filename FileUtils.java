package com.edusphere.utils;

import com.edusphere.models.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

public class FileUtils {

    private static final Gson gson = new Gson();
    private static final ObjectMapper mapper = new ObjectMapper();

    // --- Utility for file hash (MD5) ---
    public static String getFileHash(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = Files.readAllBytes(path);
        byte[] hash = digest.digest(bytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // --- Notes specific methods ---
    public static List<Note> loadNotes() {
        return loadListFromJson("src/main/resources/data/notes.json", new TypeToken<List<Note>>() {});
    }

    public static boolean isDuplicate(Note newNote) {
        List<Note> existingNotes = loadNotes();
        for (Note note : existingNotes) {
            boolean sameTitle = note.getTitle().equalsIgnoreCase(newNote.getTitle());
            boolean sameSubject = note.getSubject().equalsIgnoreCase(newNote.getSubject());
            boolean sameFile = note.getFileHash() != null && note.getFileHash().equals(newNote.getFileHash());

            if ((sameTitle && sameSubject) || sameFile) return true;
        }
        return false;
    }

    // --- Generic Gson JSON loader ---
    public static <T> List<T> loadListFromJson(String filePath, TypeToken<List<T>> typeToken) {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Type type = typeToken.getType();
            List<T> data = gson.fromJson(reader, type);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> void saveListToJson(String filePath, List<T> data) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Generic Jackson read/write for quizzes/results ---
    public static <T> List<T> readListFromFile(String path, TypeReference<List<T>> typeRef) {
        try {
            File file = new File(path);

            if (!file.exists()) {
                // Ensure parent directories exist
                Files.createDirectories(Paths.get(file.getParent()));
                mapper.writeValue(file, Collections.emptyList());
                return new ArrayList<>();
            }

            if (file.length() == 0) return new ArrayList<>();

            return mapper.readValue(file, typeRef);

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static <T> void writeListToFile(String path, List<T> data) {
        try {
            File file = new File(path);
            Files.createDirectories(Paths.get(file.getParent()));
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}