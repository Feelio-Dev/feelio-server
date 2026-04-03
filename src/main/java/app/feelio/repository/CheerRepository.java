package app.feelio.repository;

import app.feelio.dto.cheer.Cheer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheerRepository extends JpaRepository<Cheer, Long> {
    Page<Cheer> findByEmotion(String emotion, Pageable pageable);
}