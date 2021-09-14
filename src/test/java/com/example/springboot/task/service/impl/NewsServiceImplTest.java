package com.example.springboot.task.service.impl;

import com.example.springboot.task.dto.NewsDto;
import com.example.springboot.task.entity.News;
import com.example.springboot.task.mapper.NewsMapper;
import com.example.springboot.task.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    private static final Long NEWS_ONE_ID = 1L;
    private static final Long NEWS_SECOND_ID = 2L;
    private static final Long NEWS_THIRD_ID = 3L;
    private static final String NEWS_ONE_TITLE = "News one title";
    private static final String NEWS_SECOND_TITLE = "News second title";
    private static final String NEWS_THIRD_TITLE = "News third title";
    private static final String NEW_NEWS_TITLE = "New news title";
    private static final String NEWS_ONE_TEXT = "News one text";
    private static final String NEWS_SECOND_TEXT = "News second text";
    private static final String NEWS_THIRD_TEXT = "News third text";
    private static final String NEW_NEWS_TEXT = "New news text";


    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper mapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private News news1;
    private News news2;
    private News news3;
    private News newNews;

    private NewsDto newsDto1;
    private NewsDto newsDto2;
    private NewsDto newsDto3;

    private NewsDto newNewsDto;

    private List<News> newsList;


    @BeforeEach
    void setUp() {
        news1 = new News(NEWS_ONE_ID, LocalDateTime.now(), NEWS_ONE_TITLE, NEWS_ONE_TEXT);
        news2 = new News(NEWS_SECOND_ID, LocalDateTime.now(), NEWS_SECOND_TITLE, NEWS_SECOND_TEXT);
        news3 = new News(NEWS_THIRD_ID, LocalDateTime.now(), NEWS_THIRD_TITLE, NEWS_THIRD_TEXT);
        newNews = new News(NEWS_THIRD_ID, LocalDateTime.now(), NEW_NEWS_TITLE, NEW_NEWS_TEXT);


        newsDto1 = new NewsDto(NEWS_ONE_ID, LocalDateTime.now(), NEWS_ONE_TITLE, NEWS_ONE_TEXT);
        newsDto2 = new NewsDto(NEWS_SECOND_ID, LocalDateTime.now(), NEWS_SECOND_TITLE, NEWS_SECOND_TEXT);
        newsDto3 = new NewsDto(NEWS_THIRD_ID, LocalDateTime.now(), NEWS_THIRD_TITLE, NEWS_THIRD_TEXT);
        newNewsDto = new NewsDto(NEWS_THIRD_ID, LocalDateTime.now(), NEW_NEWS_TITLE, NEW_NEWS_TEXT);

        newsList = Arrays.asList(news1, news2, news3);
    }


    @Test
    void testFindAll() {
        when(newsRepository.findAll()).thenReturn(newsList);
        when(mapper.entityToDto(news1)).thenReturn(newsDto1);
        when(mapper.entityToDto(news2)).thenReturn(newsDto2);
        when(mapper.entityToDto(news3)).thenReturn(newsDto3);
        List<NewsDto> list = newsService.findAll();
        assertThat(list).hasSize(3).extracting(NewsDto::getText).containsOnly(NEWS_ONE_TEXT, NEWS_SECOND_TEXT, NEWS_THIRD_TEXT);
        assertThat(list).hasSize(3).extracting(NewsDto::getTitle).containsOnly(NEWS_ONE_TITLE, NEWS_SECOND_TITLE, NEWS_THIRD_TITLE);
    }

        @Test
    void testFindAllWithPagination() {
        Page pageMock = mock(Page.class);
        PageRequest pageRequest = PageRequest.of(0,2);
        when(mapper.entityToDto(news1)).thenReturn(newsDto1);
        when(mapper.entityToDto(news2)).thenReturn(newsDto2);
        when(mapper.entityToDto(news3)).thenReturn(newsDto3);

        when(pageMock.stream()).thenReturn(newsList.stream());
        when(newsRepository.findAll(pageRequest)).thenReturn(pageMock);

            List<NewsDto> dtoList = newsService.findAll(pageRequest);

            assertThat(dtoList).hasSize(3).extracting(NewsDto::getText).containsOnly(NEWS_ONE_TEXT, NEWS_SECOND_TEXT, NEWS_THIRD_TEXT);
            assertThat(dtoList).hasSize(3).extracting(NewsDto::getTitle).containsOnly(NEWS_ONE_TITLE, NEWS_SECOND_TITLE, NEWS_THIRD_TITLE);
        }

    @Test
    void testFindById() {
        when(newsRepository.findById(NEWS_ONE_ID)).thenReturn(Optional.of(news1));
        Optional<NewsDto> newsDto = newsService.findById(NEWS_ONE_ID);
        verify(newsRepository, times(1)).findById(anyLong());
        assertNotNull(newsDto);
    }


    @Test
    void testCreate() {
        when(newsRepository.save(mapper.dtoToEntity(newsDto1))).thenReturn(news1);
        newsService.create(mapper.entityToDto(news1));
        verify(newsRepository, times(1)).save(mapper.dtoToEntity(newsDto1));
    }

    @Test
    void testUpdate() {
        when(newsRepository.findById(NEWS_THIRD_ID)).thenReturn(Optional.of(news3));
        when(mapper.entityToDto(newNews)).thenReturn(newNewsDto);
        when(newsRepository.save(mapper.dtoToEntity(newNewsDto))).thenReturn(newNews);
        newsService.update(mapper.entityToDto(newNews));
        verify(newsRepository, times(1)).save(mapper.dtoToEntity(newNewsDto));

    }

    @Test
    void testDelete() {
        when(newsRepository.findById(NEWS_ONE_ID)).thenReturn(Optional.of(news1));
        newsService.delete(news1.getId());
        verify(newsRepository, times(1)).deleteById(NEWS_ONE_ID);

    }
}