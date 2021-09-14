package com.example.springboot.task.repository;

import com.example.springboot.task.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByNewsId(Long newsId);

    List<Comment> findByNewsId(Long newsId, Pageable pageable);

    Optional<Comment> findByNewsIdAndId(Long newsId, Long id);

    void deleteByNewsIdAndId(Long newsId, Long id);
}
