package com.dio.budgetapi.controller;

import com.dio.budgetapi.service.AiAssistantService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final AiAssistantService aiService;

    public BudgetController(AiAssistantService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(value = "/text", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> handleText(@RequestBody Map<String, String> request) {
        String response = aiService.processTextCommand(request.get("message"));
        return ResponseEntity.ok(Map.of("reply", response));
    }

    @PostMapping(value = "/voice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> handleVoice(@RequestParam("audio") MultipartFile file) throws IOException {
        InputStreamResource resource = new InputStreamResource(file.getInputStream());
        byte[] audioResponse = aiService.processAudioCommand(resource);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resposta.mp3\"")
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(audioResponse);
    }
}