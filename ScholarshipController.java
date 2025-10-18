package com.edusphere.controllers;

import com.edusphere.utils.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;

public class ScholarshipController {

    private static final String SCHOLARSHIP_FILE = "src/main/resources/data/scholarships.json";

    public static void registerRoutes(Javalin app) {
        app.get("/api/scholarships", ScholarshipController::getAllScholarships);
    }

    public static void getAllScholarships(Context ctx) {
        try {
            List<Map<String, Object>> scholarships = FileUtils.readListFromFile(
                    SCHOLARSHIP_FILE, new TypeReference<List<Map<String, Object>>>() {}
            );
            ctx.json(scholarships);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error fetching scholarships: " + e.getMessage());
        }
    }
}
