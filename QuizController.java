package com.edusphere.controllers;

import com.edusphere.models.Quiz;
import com.edusphere.models.Question;
import com.edusphere.utils.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;

public class QuizController {

    private static final String QUIZ_FILE = "src/main/resources/data/quizzes.json";
    private static final String RESULTS_FILE = "src/main/resources/data/results.json";

    public static void registerRoutes(Javalin app) {
        app.get("/api/quizzes", QuizController::getAllQuizzes);
        app.post("/api/submit-quiz", QuizController::submitQuiz);
        app.post("/api/add-quiz", ctx -> addQuiz(ctx)); // safer lambda
        app.get("/api/results", QuizController::getUserResults); // register the results endpoint
    }

    // Fetch all quizzes
    public static void getAllQuizzes(Context ctx) {
        List<Quiz> quizzes = FileUtils.readListFromFile(QUIZ_FILE, new TypeReference<List<Quiz>>() {});
        ctx.json(quizzes);
    }

    // Submit quiz answers
    // QuizController.java - submitQuiz
    public static void submitQuiz(Context ctx) {
        try {
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            if (body.get("quizId") == null || body.get("answers") == null) {
                ctx.status(400).result("Missing quizId or answers");
                return;
            }

            int quizId = Integer.parseInt(body.get("quizId").toString());
            List<?> answersRaw = (List<?>) body.get("answers");
            List<String> answers = new ArrayList<>();
            for (Object a : answersRaw) answers.add(a.toString().trim());

            List<Quiz> quizzes = FileUtils.readListFromFile(QUIZ_FILE, new TypeReference<List<Quiz>>() {});
            Quiz quiz = quizzes.stream().filter(q -> q.getId() == quizId).findFirst().orElse(null);
            if (quiz == null) {
                ctx.status(404).result("Quiz not found");
                return;
            }

            int score = 0;
            List<Question> questions = quiz.getQuestions();
            for (int i = 0; i < questions.size(); i++) {
                if (i < answers.size() && questions.get(i).getAnswer().trim().equalsIgnoreCase(answers.get(i))) {
                    score++;
                }
            }

            // Save result
            List<Map<String, Object>> results = FileUtils.readListFromFile(
                    RESULTS_FILE, new TypeReference<List<Map<String, Object>>>() {}
            );

            Map<String, Object> resultEntry = new HashMap<>();
            resultEntry.put("username", "demoUser"); // TODO: replace with real user
            resultEntry.put("quizId", quizId);
            resultEntry.put("title", quiz.getTitle());        // store title
            resultEntry.put("subject", quiz.getSubject());    // store subject
            resultEntry.put("score", score);
            resultEntry.put("total", questions.size());
            resultEntry.put("timestamp", new Date().toInstant().toString()); // ISO string

            results.add(resultEntry);
            FileUtils.writeListToFile(RESULTS_FILE, results);

            ctx.json(Map.of("message", "Result saved", "score", score, "total", questions.size()));

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400).result("Error saving result: " + e.getMessage());
        }
    }

    // Add new quiz
    public static void addQuiz(Context ctx) {
        try {
            Quiz quiz = ctx.bodyAsClass(Quiz.class);
            List<Quiz> quizzes = FileUtils.readListFromFile(QUIZ_FILE, new TypeReference<List<Quiz>>() {});
            quizzes.add(quiz);
            FileUtils.writeListToFile(QUIZ_FILE, quizzes);
            ctx.status(201).json(Map.of("message", "Quiz added successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400).result("Error adding quiz: " + e.getMessage());
        }
    }

    // Fetch quiz results for the user
    public static void getUserResults(Context ctx) {
        try {
            String username = "demoUser"; // replace with real authenticated user

            List<Map<String, Object>> results = FileUtils.readListFromFile(
                    RESULTS_FILE, new TypeReference<List<Map<String, Object>>>() {}
            );

            List<Quiz> quizzes = FileUtils.readListFromFile(
                    QUIZ_FILE, new TypeReference<List<Quiz>>() {}
            );

            Map<Integer, Quiz> quizMap = new HashMap<>();
            for (Quiz q : quizzes) {
                quizMap.put(q.getId(), q);
            }

            List<Map<String, Object>> userResults = new ArrayList<>();
            for (Map<String, Object> r : results) {
                if (username.equals(r.get("username"))) {
                    int quizId = (int) r.get("quizId");
                    Quiz quiz = quizMap.get(quizId);
                    if (quiz == null) continue;

                    Map<String, Object> entry = new HashMap<>(r);
                    entry.put("title", quiz.getTitle());
                    entry.put("subject", quiz.getSubject());
                    userResults.add(entry);
                }
            }

            // Sort by timestamp ascending (so line graphs make sense)
            userResults.sort((a, b) -> {
                String t1 = a.get("timestamp").toString();
                String t2 = b.get("timestamp").toString();
                return t1.compareTo(t2);
            });

            ctx.json(userResults);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error fetching results: " + e.getMessage());
        }
    }

}
