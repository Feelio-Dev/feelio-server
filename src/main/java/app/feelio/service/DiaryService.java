package app.feelio.service;


import app.feelio.dto.cheer.Cheer;
import app.feelio.dto.diary.Diary;
import app.feelio.repository.CheerRepository;
import app.feelio.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final CheerRepository cheerRepository;


    public List<Diary> getMonthlyDiaries(Long userId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);
        return diaryRepository.findByUserIdAndCreatedAtBetween(userId, start, end);
    }

    @Transactional
    public Diary updateEmotion(Long userId, String emotion, Integer score) {
        Diary diary = Diary.builder()
                .userId(userId)
                .emotion(emotion)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();
        return diaryRepository.save(diary);
    }

    @Transactional
    public Cheer createCheer(Long userId, String emotion, String content) {
        Cheer cheer = Cheer.builder()
                .userId(userId)
                .nickname("유저" + userId)
                .emotion(emotion)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return cheerRepository.save(cheer);
    }

    public Page<Cheer> getCheers(String emotion, Pageable pageable) {
        return cheerRepository.findByEmotion(emotion, pageable);
    }
}