package com.example.springboot.task.service.impl;

import com.example.springboot.task.dto.CommentDto;
import com.example.springboot.task.dto.NewsDto;
import com.example.springboot.task.entity.Comment;
import com.example.springboot.task.entity.News;
import com.example.springboot.task.mapper.CommentMapper;
import com.example.springboot.task.repository.CommentRepository;
import com.example.springboot.task.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    private static final Long COMMENT_ONE_ID = 1L;
    private static final Long COMMENT_SECOND_ID = 2L;
    private static final Long COMMENT_THIRD_ID = 3L;
    private static final String COMMENT_ONE_TEXT = "One comment Text";
    private static final String COMMENT_SECOND_TEXT = "Second comment Text";
    private static final String COMMENT_THIRD_TEXT = "Third comment Text";
    private static final String NEW_COMMENT_TEXT = "New comment Text";
    private static final String COMMENT_ONE_USER = "Ivan";
    private static final String COMMENT_SECOND_USER = "Aleksandr";
    private static final String COMMENT_THIRD_USER = "Petr";
    private static final String NEW_COMMENT_USER = "New User";
    private static final Long NEWS_ONE_ID = 1L;
    private static final String NEWS_ONE_TITLE = "News one title";
    private static final String NEWS_ONE_TEXT = "News one text";


    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper mapper;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private Comment newComment;

    private News news1;
    private NewsDto newsDto1;

    private CommentDto commentDto1;
    private CommentDto commentDto2;
    private CommentDto commentDto3;
    private CommentDto newCommentDto;

    private List<Comment> listComment;

    @BeforeEach
    void setUp() {
        comment1 = new Comment(COMMENT_ONE_ID, LocalDateTime.now(), COMMENT_ONE_TEXT, COMMENT_ONE_USER);
        comment2 = new Comment(COMMENT_SECOND_ID, LocalDateTime.now(), COMMENT_SECOND_TEXT, COMMENT_SECOND_USER);
        comment3 = new Comment(COMMENT_THIRD_ID, LocalDateTime.now(), COMMENT_THIRD_TEXT, COMMENT_THIRD_USER);
        newComment = new Comment(COMMENT_ONE_ID, LocalDateTime.now(), NEW_COMMENT_TEXT, NEW_COMMENT_USER);

        news1 = new News(NEWS_ONE_ID, LocalDateTime.now(), NEWS_ONE_TITLE, NEWS_ONE_TEXT);
        newsDto1 = new NewsDto(NEWS_ONE_ID, LocalDateTime.now(), NEWS_ONE_TITLE, NEWS_ONE_TEXT);

        commentDto1 = new CommentDto(COMMENT_ONE_ID, LocalDateTime.now(), COMMENT_ONE_TEXT, COMMENT_ONE_USER);
        commentDto2 = new CommentDto(COMMENT_SECOND_ID, LocalDateTime.now(), COMMENT_SECOND_TEXT, COMMENT_SECOND_USER);
        commentDto3 = new CommentDto(COMMENT_THIRD_ID, LocalDateTime.now(), COMMENT_THIRD_TEXT, COMMENT_THIRD_USER);
        newCommentDto = new CommentDto(COMMENT_ONE_ID, LocalDateTime.now(), NEW_COMMENT_TEXT, NEW_COMMENT_USER);

        listComment = Arrays.asList(comment1, comment2, comment3);
    }


    @Test
    void testFindAll() {
        when(newsRepository.findById(NEWS_ONE_ID)).thenReturn(Optional.of(news1));
        when(commentRepository.findByNewsId(NEWS_ONE_ID)).thenReturn(listComment);
        when(mapper.entityToDto(comment1)).thenReturn(commentDto1);
        when(mapper.entityToDto(comment2)).thenReturn(commentDto2);
        when(mapper.entityToDto(comment3)).thenReturn(commentDto3);
        List<CommentDto> list = commentService.findAll(NEWS_ONE_ID);
        assertThat(list).hasSize(3).extracting(CommentDto::getText).containsOnly(COMMENT_ONE_TEXT, COMMENT_SECOND_TEXT, COMMENT_THIRD_TEXT);
        assertThat(list).hasSize(3).extracting(CommentDto::getUserName).containsOnly(COMMENT_ONE_USER, COMMENT_SECOND_USER, COMMENT_THIRD_USER);
    }

    @Test
    void testFindById() {
        when(newsRepository.findById(NEWS_ONE_ID)).thenReturn(Optional.of(news1));
        Optional<CommentDto> commentDto = commentService.findById(news1.getId(), comment1.getId());
        verify(commentRepository, times(1)).findByNewsIdAndId(NEWS_ONE_ID, COMMENT_ONE_ID);
        assertNotNull(commentDto);
    }

    @Test
    void testCreate() {
        when(newsRepository.findById(NEWS_ONE_ID)).thenReturn(Optional.of(news1));
        when(mapper.dtoToEntity(newCommentDto)).thenReturn(newComment);
        when(mapper.entityToDto(newComment)).thenReturn(newCommentDto);
        newComment.setNews(news1);
        commentService.create(NEWS_ONE_ID,mapper.entityToDto(newComment));
        verify(commentRepository, times(1)).save(mapper.dtoToEntity(newCommentDto));
    }


    @Test
    void testUpdate() {
        when(newsRepository.findById(NEWS_ONE_ID)).thenReturn(Optional.of(news1));
        when(commentRepository.findById(COMMENT_ONE_ID)).thenReturn(Optional.of(comment1));
        when(mapper.dtoToEntity(newCommentDto)).thenReturn(newComment);
        when(mapper.entityToDto(newComment)).thenReturn(newCommentDto);
        newComment.setNews(news1);
        when(commentRepository.save(newComment)).thenReturn(newComment);
        commentService.update(NEWS_ONE_ID, mapper.entityToDto(newComment));
        verify(commentRepository, times(1)).save(mapper.dtoToEntity(newCommentDto));


    }


    @Test
    void testDelete() {
        when(newsRepository.findById(NEWS_ONE_ID)).thenReturn(Optional.of(news1));
        when(commentRepository.findById(COMMENT_ONE_ID)).thenReturn(Optional.of(comment1));
        commentService.delete(news1.getId(), comment1.getId());
        verify(commentRepository, times(1)).deleteByNewsIdAndId(news1.getId(), comment1.getId());
    }
}