package com.FlagHome.backend.domain.reply.controller;

import com.FlagHome.backend.domain.HttpResponse;
import com.FlagHome.backend.domain.reply.dto.ReplyDto;
import com.FlagHome.backend.domain.reply.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "reply", description = "댓글 API")
@RestController
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @Tag(name = "reply")
    @Operation(summary = "댓글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 생성을 완료 하였습니다."),
            @ApiResponse(responseCode = "404", description = "게시글 또는 유저를 찾을 수 없습니다.")
    })
    @PostMapping
    public ResponseEntity<HttpResponse> createReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.ok(HttpResponse.ok(replyService.createReply(replyDto), HttpStatus.CREATED, "댓글 생성을 완료 하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글리스트 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 리스트 가져오기에 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.")
    })
    @GetMapping
    public ResponseEntity<HttpResponse> getReplies(@RequestParam(name = "id") long postId) {
        return ResponseEntity.ok(HttpResponse.ok(replyService.findReplies(postId), HttpStatus.OK, "댓글 리스트 가져오기에 성공 하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "204", description = "댓글 삭제에 성공하였습니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> deleteReply(@PathVariable(name = "id") long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.ok(HttpResponse.ok(true, HttpStatus.NO_CONTENT, "댓글 삭제에 성공하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.")
    })
    @PatchMapping
    public ResponseEntity<HttpResponse> updateReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.ok(HttpResponse.ok(replyService.updateReply(replyDto), HttpStatus.OK, "댓글 수정에 성공 하였습니다."));
    }
}