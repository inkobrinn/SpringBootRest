package com.example.springboot.task.dto;

import com.example.springboot.task.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {
    Long id;
    LocalDateTime date;
    String title;
    String text;
    List<Comment> commentList;

    public NewsDto(Long id, LocalDateTime date, String title, String text) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.text = text;
    }
}
