package com.edusphere;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import com.edusphere.controllers.NoteController;
import com.edusphere.controllers.QuizController;
import com.edusphere.controllers.ScholarshipController;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/uploads"; // accessible from /uploads/*
                staticFiles.directory = "uploads";   // actual folder on your system
                staticFiles.location = Location.EXTERNAL; // not inside resources
            });
        }).start(7000);

        System.out.println("✅ Server started on http://localhost:7000");

        // Register note routes
        NoteController.registerRoutes(app);
        QuizController.registerRoutes(app);
        ScholarshipController.registerRoutes(app);

    }
}

