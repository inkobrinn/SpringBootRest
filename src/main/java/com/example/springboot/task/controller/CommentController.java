package com.example.springboot.task.controller;

import com.example.springboot.task.dto.CommentDto;
import com.example.springboot.task.service.CommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.example.springboot.task.util.UtilConstants.DEFAULT_PAGE_NUMBER;
import static com.example.springboot.task.util.UtilConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/news/{newsId}/comments")
public class CommentController {

    private final CommentService commentService;


    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Make a request to receive all comments with pagination
     *
     * @param newsId for check news
     * @param page   zero-based page index, must not be negative.
     * @param size   the size of the page to be returned, must be greater than 0.
     * @return a list of objects comments with pagination, if any, otherwise an empty list
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "Comments has been successfully found"),
            @ApiResponse(code = 404, message = "Comments not found")
    })
    @ApiOperation("Search all comments with pagination")
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<CommentDto>> findAllComments(@PathVariable Long newsId,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_NUMBER) Integer page,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        List<CommentDto> list = commentService.findAll(newsId, page, size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Make a request to receive all comments
     *
     * @param newsId for check news
     * @return a list of objects comments, if any, otherwise an empty list
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "Comments has been successfully found"),
            @ApiResponse(code = 404, message = "Comments not found")
    })
    @ApiOperation("Search all comments")
    @GetMapping
    public ResponseEntity<List<CommentDto>> findAllComments(@PathVariable Long newsId) {
        List<CommentDto> commentDtoList = commentService.findAll(newsId);
        return new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    /**
     * Search for comments by id
     *
     * @param newsId for check news
     * @param id     the parameter by which we search for comments
     * @return a comments object if any, otherwise HttpStatus.NOT_FOUND)
     */
    @ApiResponses({
            @ApiResponse(code = 200, message = "Comment has been successfully found"),
            @ApiResponse(code = 404, message = "Comment not found")
    })
    @ApiOperation("Search comments by id")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<CommentDto>> findCommentById(@PathVariable Long newsId, @PathVariable Long id) {
        Optional<CommentDto> dtoOptional = commentService.findById(newsId, id);
        return dtoOptional.map(commentDto -> new ResponseEntity<>(dtoOptional, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Saving object comments
     *
     * @param newsId     for check news
     * @param commentDto object comments to save
     * @return saved comments with HttpStatus.CREATED
     */
    @ApiResponses({
            @ApiResponse(code = 201, message = "Comment has been successfully created"),
            @ApiResponse(code = 400, message = "Incorrect data")
    })
    @ApiOperation("Saving comments")
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long newsId, @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.create(newsId, commentDto), HttpStatus.CREATED);
    }

    /**
     * Update object comments
     *
     * @param newsId     for check news
     * @param commentDto object comments to update
     * @return updated object comments
     */
    @ApiResponses({
            @ApiResponse(code = 201, message = "Comment has been updated"),
            @ApiResponse(code = 400, message = "Incorrect data")
    })
    @ApiOperation("Update comments")
    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long newsId, @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.update(newsId, commentDto), HttpStatus.OK);
    }

    /**
     * Delete object comments by id
     *
     * @param newsId for check news
     * @param id     for check comments and delete object comments
     * @return HttpStatus.NO_CONTENT on successful deletion
     */
    @ApiResponses({
            @ApiResponse(code = 204, message = "Comment has been deleted, no content"),
            @ApiResponse(code = 400, message = "Incorrect data")
    })
    @ApiOperation("Deletion comments by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long newsId, @PathVariable Long id) {
        commentService.delete(newsId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
