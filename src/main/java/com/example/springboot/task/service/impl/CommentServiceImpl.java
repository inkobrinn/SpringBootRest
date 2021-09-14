package com.example.springboot.task.service.impl;

import com.example.springboot.task.dto.CommentDto;
import com.example.springboot.task.entity.Comment;
import com.example.springboot.task.entity.News;
import com.example.springboot.task.exception.CommentNotFoundException;
import com.example.springboot.task.exception.NewsNotFoundException;
import com.example.springboot.task.mapper.CommentMapper;
import com.example.springboot.task.repository.CommentRepository;
import com.example.springboot.task.repository.NewsRepository;
import com.example.springboot.task.service.CommentService;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.springboot.task.exception.ExceptionMessage.COMMENT_NOT_FOUND;
import static com.example.springboot.task.exception.ExceptionMessage.NEWS_NOT_FOUND;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final NewsRepository newsRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper mapper, NewsRepository newsRepository) {
        this.commentRepository = commentRepository;
        this.mapper = mapper;
        this.newsRepository = newsRepository;
    }

    /**
     * Saving object comments
     *
     * @param newsId     for check news
     * @param commentDto object comments to save
     * @return saved comments
     */
    @Transactional
    @Override
    public CommentDto create(Long newsId, CommentDto commentDto) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + newsId));
        Comment comment = mapper.dtoToEntity(commentDto);
        comment.setNews(news);
        Comment saveComment = commentRepository.save(comment);
        return mapper.entityToDto(saveComment);
    }

    /**
     * Make a request to receive all comments
     *
     * @param newsId for check news
     * @return a list of comments
     */
    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAll(Long newsId) {
        newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + newsId));
        List<Comment> commentList = commentRepository.findByNewsId(newsId);
        return commentList.stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    /**
     * Make a request to receive all comments
     *
     * @param newsId for check news
     * @param page   zero-based page index, must not be negative.
     * @param size   the size of the page to be returned, must be greater than 0.
     * @return a list of comments
     */
    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAll(Long newsId, int page, int size) {
        newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + newsId));
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        List<Comment> commentList = commentRepository.findByNewsId(newsId, pageRequest);
        return commentList.stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    /**
     * Search for comments by id
     *
     * @param newsId for check news and search comments
     * @param id     the parameter by which we search for comments
     * @return a comments object
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findById(Long newsId, Long id) {
        newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + newsId));
        return commentRepository.findByNewsIdAndId(newsId, id).map(mapper::entityToDto);

    }

    /**
     * Update object comments
     *
     * @param id         for check news
     * @param commentDto object comments to update
     * @return updated object comments
     */
    @Transactional
    @Override
    public CommentDto update(Long id, CommentDto commentDto) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + id));
        commentRepository.findById(commentDto.getId()).orElseThrow(() -> new CommandAcceptanceException(COMMENT_NOT_FOUND + commentDto.getId()));
        Comment comment = mapper.dtoToEntity(commentDto);
        comment.setNews(news);
        Comment saveComment = commentRepository.save(comment);
        return mapper.entityToDto(saveComment);
    }

    /**
     * Delete object comments by id
     *
     * @param newsId for check news
     * @param id     for check comments and delete object comments
     */
    @Transactional
    @Override
    public void delete(Long newsId, Long id) {
        newsRepository.findById(newsId).orElseThrow(() -> new NewsNotFoundException(NEWS_NOT_FOUND + id));
        commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND + id));
        commentRepository.deleteByNewsIdAndId(newsId, id);

    }
}
