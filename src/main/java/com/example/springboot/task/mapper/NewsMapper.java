package com.example.springboot.task.mapper;

import com.example.springboot.task.dto.NewsDto;
import com.example.springboot.task.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    /**
     * converting object News to object NewsDto
     *
     * @param news convertible object
     * @return mapped object
     */
    NewsDto entityToDto(News news);

    /**
     * converting object NewsDto to object News
     *
     * @param newsDto convertible object
     * @return mapped object
     */
    News dtoToEntity(NewsDto newsDto);
}
