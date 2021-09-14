package com.example.springboot.task.mapper;

import com.example.springboot.task.dto.CommentDto;
import com.example.springboot.task.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /**
     * converting object Comments to object CommentsDto
     *
     * @param comment convertible object
     * @return mapped object
     */
    CommentDto entityToDto(Comment comment);

    /**
     * converting object CommentsDto to object Comments
     *
     * @param commentDto convertible object
     * @return mapped object
     */
    Comment dtoToEntity(CommentDto commentDto);

}
