package com.fitness.aiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.dto.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String responseBody = geminiService.getAnswer(prompt);
        return processResponse(activity, responseBody);
    }

    private Recommendation processResponse(Activity activity, String responseBody) {

        try {
            String parsed = parseJson(responseBody);

            log.info("AI RECOMMENDATION: {}", parsed);

            JsonNode parsedResponse = objectMapper.readTree(parsed);
            JsonNode analysisNode = parsedResponse.path("analysis");
            JsonNode improvementsNode = parsedResponse.path("improvements");
            JsonNode suggestionsNode = parsedResponse.path("suggestions");
            JsonNode safetyNode = parsedResponse.path("safety");

            String fullAnalysis = extractAnalysis(analysisNode);
            List<String> improvements = extractImprovements(improvementsNode);
            List<String> suggestions = extractSuggestions(suggestionsNode);
            List<String> safety = extractSafety(safetyNode);

            return Recommendation.builder()
                    .activityId(activity.id())
                    .userId(activity.userId())
                    .activityType(activity.type())
                    .recommendation(fullAnalysis)
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(Instant.now())
                    .build();
        } catch (Exception e) {
            log.error("Error processing response", e);
            return defaultRecommendation(activity);
        }
    }

    private String extractAnalysis(JsonNode analysisNode) {
        StringBuilder fullAnalysis = new StringBuilder();
        addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall: ");
        addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace: ");
        addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate: ");
        addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned: ");
        return fullAnalysis.toString().trim();
    }

    private Recommendation defaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.id())
                .userId(activity.userId())
                .activityType(activity.type())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness professional"))
                .safety(List.of(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"))
                .createdAt(Instant.now())
                .build();
    }

    private String parseJson(String responseBody) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode textNode = rootNode.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text");

        return textNode.asText()
                .replaceAll("```json\\n", "")
                .replaceAll("\\n```", "")
                .trim();
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()) {
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific improvements provided") :
                improvements;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()) {
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided") :
                suggestions;
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if (safetyNode.isArray()) {
            safetyNode.forEach(s -> {
                safety.add(s.asText());
            });
        }
        return safety.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines") :
                safety;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if (!analysisNode.path(key).isMissingNode()) {
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                        {
                            "analysis": {
                                "overall": "Overall analysis here",
                                "pace": "Pace analysis here",
                                "heartRate": "Heart rate analysis here",
                                "caloriesBurned": "Calories analysis here"
                            },
                            "improvements": [
                                {
                                    "area": "Area name",
                                    "recommendation": "Detailed recommendation"
                                }
                            ],
                            "suggestions": [
                                {
                                    "workout": "Workout name",
                                    "description": "Detailed workout description"
                                }
                            ],
                            "safety": [
                                "safety point 1",
                                "safety point 2"
                            ]
                        }
                        
                        Analyze this activity:
                        Activity Type: %s
                        Duration: %d minutes
                        Calories Burned: %d
                        Additional Metrics: %s
                        
                        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety
                        guidelines.
                        IMPORTANT: Ensure the response follows the EXACT JSON format shown above.
                        """,
                activity.type(),
                activity.duration(),
                activity.caloriesBurned(),
                activity.additionalMetrics()
        );
    }
}
