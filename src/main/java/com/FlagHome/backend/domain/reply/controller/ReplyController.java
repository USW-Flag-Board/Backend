package com.FlagHome.backend.domain.reply.controller;

import com.FlagHome.backend.domain.reply.controller.dto.CreateReplyRequest;
import com.FlagHome.backend.domain.reply.controller.dto.ReplyResponse;
import com.FlagHome.backend.domain.reply.controller.dto.UpdateReplyRequest;
import com.FlagHome.backend.domain.reply.service.ReplyService;
import com.FlagHome.backend.global.common.ApplicationResponse;
import com.FlagHome.backend.global.utility.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "reply", description = "댓글 API")
@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    /**
     * Version 1
     */
    /*@Tag(name = "reply")
    @Operation(summary = "댓글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 생성을 완료 하였습니다."),
            @ApiResponse(responseCode = "404", description = "게시글 또는 유저를 찾을 수 없습니다.")
    })
    @PostMapping
    public ResponseEntity<ApplicationResponse> createReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.ok(ApplicationResponse.of(replyService.createReply(replyDto), HttpStatus.CREATED, "댓글 생성을 완료 하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글리스트 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 리스트 가져오기에 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.")
    })
    @GetMapping
    public ResponseEntity<ApplicationResponse> getReplies(@RequestParam(name = "post-id") long postId) {
        return ResponseEntity.ok(ApplicationResponse.of(replyService.findReplies(postId), HttpStatus.OK, "댓글 리스트 가져오기에 성공 하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글 삭제")
    @ApiResponse(responseCode = "204", description = "댓글 삭제에 성공하였습니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponse> deleteReply(@PathVariable(name = "id") long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.NO_CONTENT, "댓글 삭제에 성공하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.")
    })
    @PatchMapping
    public ResponseEntity<ApplicationResponse> updateReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.ok(ApplicationResponse.of(replyService.updateReply(replyDto), HttpStatus.OK, "댓글 수정에 성공 하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글 좋아요",
            description = "target-id = 좋아요를 할 댓글의 Id\n\n" +
                    "member-id = 서버에서 준 member의 고유ID를 넣으면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 좋아요를 하였습니다."),
            @ApiResponse(responseCode = "400", description = "댓글 좋아요 에러가 발생하였습니다.")
    })
    @PostMapping("/like")
    public ResponseEntity<ApplicationResponse> likeReply(@RequestBody LikeDto likeDto) {
        likeService.like(likeDto.getMemberId(), likeDto.getTargetId(), LikeType.REPLY);
        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.OK, "댓글 좋아요를 하였습니다."));
    }

    @Tag(name = "reply")
    @Operation(summary = "댓글 좋아요 취소",
            description = "target-id = 좋아요를 할 댓글의 Id\n\n" +
                    "member-id = 서버에서 준 member의 고유ID를 넣으면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 좋아요 취소를 하였습니다."),
            @ApiResponse(responseCode = "400", description = "댓글 좋아요 취소 에러가 발생하였습니다.")
    })
    @DeleteMapping("/like")
    public ResponseEntity<ApplicationResponse> unlikePost(@RequestParam(value = "member-id") long memberId,
                                                  @RequestParam(value = "target-id") long targetId) {
        likeService.unlike(memberId, targetId, LikeType.REPLY);
        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.NO_CONTENT, "댓글 좋아요를 취소 하였습니다."));
    }*/
    /**
     * Version 2
     */
    @Tag(name = "reply")
    @Operation(summary = "게시글 댓글 가져오기", description = "<삭제된 게시글>로 보이는 값은 안 보이게 하면 된다.")
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<List<ReplyResponse>> getAllReplies(@RequestParam("post") Long postId) {
        return new ApplicationResponse<>(replyService.getAllReplies(postId));
    }

    @Tag(name = "reply")
    @ResponseStatus(CREATED)
    @PostMapping
    public ApplicationResponse create(@RequestBody @Valid CreateReplyRequest request) {
        replyService.create(SecurityUtils.getMemberId(), request.getPostId(), request.getContent());
        return new ApplicationResponse<>();
    }

    @Tag(name = "reply")
    @ResponseStatus(OK)
    @PutMapping("/{id}")
    public ApplicationResponse update(@PathVariable Long id,
                                      @RequestBody @Valid UpdateReplyRequest request) {
        replyService.update(SecurityUtils.getMemberId(), id, request.getNewContent());
        return new ApplicationResponse<>();
    }

    @Tag(name = "reply")
    @ResponseStatus(OK)
    @DeleteMapping("/{id}")
    public ApplicationResponse delete(@PathVariable Long id) {
        replyService.delete(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>();
    }
}