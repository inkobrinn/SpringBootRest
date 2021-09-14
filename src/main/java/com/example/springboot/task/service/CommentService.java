package com.example.springboot.task.service;

import com.example.springboot.task.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    CommentDto create(Long newsId, CommentDto commentDto);

    List<CommentDto> findAll(Long newsId);

    List<CommentDto> findAll(Long newsId, int page, int size);

    Optional<CommentDto> findById(Long newsId, Long id);

    CommentDto update(Long id, CommentDto commentDto);

    void delete(Long newsId, Long id);
}
