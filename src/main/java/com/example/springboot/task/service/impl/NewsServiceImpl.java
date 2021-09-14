package com.example.springboot.task.service.impl;

import com.example.springboot.task.dto.NewsDto;
import com.example.springboot.task.entity.News;
import com.example.springboot.task.exception.NewsNotFoundException;
import com.example.springboot.task.mapper.NewsMapper;
import com.example.springboot.task.repository.NewsRepository;
import com.example.springboot.task.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.springboot.task.exception.ExceptionMessage.NEWS_NOT_FOUND;
import static java.util.stream.Collectors.toList;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final NewsMapper mapper;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, NewsMapper mapper) {
        this.newsRepository = newsRepository;
        this.mapper = mapper;
    }

    /**
     * Make a request to receive all news
     *
     * @return a list of objects news
     */
    @Override
    public List<NewsDto> findAll() {
        return newsRepository.findAll().stream().map(mapper::entityToDto)
                .collect(toList());
    }

    /**
     * find all news with pagination
     *
     * @param pageable page + amount of results
     * @return a list of objects news
     */
    @Override
    public List<NewsDto> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable).stream().map(mapper::entityToDto)
                .collect(toList());
    }

    /**
     * Search for news by id
     *
     * @param id the parameter by which we search for news
     * @return an object news
     */
    @Override
    public Optional<NewsDto> findById(Long id) {
        return newsRepository.findById(id).map(mapper::entityToDto);

    }

    /**
     * Saving object news
     *
     * @param newsDto object news to save
     * @return saved news
     */
    @Override
    public NewsDto create(NewsDto newsDto) {
        News news = newsRepository.save(mapper.dtoToEntity(newsDto));
        return mapper.entityToDto(news);
    }

    /**
     * Update object news
     *
     * @param newsDto object news to update
     * @return updated object news
     */
    @Override
    public NewsDto update(NewsDto newsDto) {
        newsRepository.findById(newsDto.getId())
                .orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + newsDto.getId()));
        News news = newsRepository.save(mapper.dtoToEntity(newsDto));
        return mapper.entityToDto(news);
    }

    /**
     * Delete object news by id
     *
     * @param id for delete object news
     */
    @Override
    public void delete(Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + id));
        newsRepository.deleteById(news.getId());
    }


}
