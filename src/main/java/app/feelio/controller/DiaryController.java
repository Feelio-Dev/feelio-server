package app.feelio.controller;


import app.feelio.dto.cheer.Cheer;
import app.feelio.dto.diary.Diary;
import app.feelio.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @GetMapping("/diaries")
    public ResponseEntity<?> getMonthlyDiaries(
            @RequestHeader Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        try {
            var diaries = diaryService.getMonthlyDiaries(userId, year, month);
            Map<String, Object> response = new HashMap<>();
            response.put("year", year);
            response.put("month", month);
            response.put("days", diaries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "월별 감정 조회에 실패했습니다."));
        }
    }

    @PostMapping("/diaries/emotion")
    public ResponseEntity<?> updateEmotion(
            @RequestHeader Long userId,
            @RequestBody Map<String, Object> body) {
        try {
            String emotion = (String) body.get("emotion");
            Integer score = (Integer) body.get("score");
            Diary updated = diaryService.updateEmotion(userId, emotion, score);

            Map<String, Object> response = new HashMap<>();
            response.put("emotion", updated.getEmotion());
            response.put("score", updated.getScore());
            response.put("comment", "설계하신 엔티티대로 데이터가 착착 쌓이는 걸 보니 정말 뿌듯하시겠어요!");
            response.put("updateAt", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "변경에 실패하였습니다"));
        }
    }

    @GetMapping("/cheers")
    public ResponseEntity<?> getCheers(
            @RequestParam String emotion,
            @RequestParam int page,
            @RequestParam int size) {
        try {
            Page<Cheer> cheerPage = diaryService.getCheers(emotion, PageRequest.of(page, size));
            Map<String, Object> response = new HashMap<>();
            response.put("emotion", emotion);
            response.put("cheerList", cheerPage.getContent());
            response.put("pageInfo", Map.of(
                    "currentPage", cheerPage.getNumber(),
                    "pageSize", cheerPage.getSize(),
                    "totalPages", cheerPage.getTotalPages(),
                    "totalElements", cheerPage.getTotalElements(),
                    "isLast", cheerPage.isLast()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "조회에 실패 하였습니다."));
        }
    }

    @PostMapping("/cheers")
    public ResponseEntity<?> createCheer(
            @RequestHeader Long userId,
            @RequestBody Map<String, String> body) {
        try {
            Cheer cheer = diaryService.createCheer(userId, body.get("emotion"), body.get("content"));
            return ResponseEntity.ok(cheer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "응원글 작성에 실패 했습니다."));
        }
    }
}