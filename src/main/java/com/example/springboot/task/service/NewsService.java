package com.example.springboot.task.service;

import com.example.springboot.task.dto.NewsDto;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface NewsService {

    NewsDto create(NewsDto newsDto);

    List<NewsDto> findAll();

    List<NewsDto> findAll(Pageable pageable);

    Optional<NewsDto> findById(Long id);

    NewsDto update(NewsDto newsDto);

    void delete(Long id);


}
