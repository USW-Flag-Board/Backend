//package com.FlagHome.backend.domain.post.reply.controller;
//
//import com.FlagHome.backend.domain.post.reply.service.ReplyService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Tag(name = "reply", description = "댓글 API")
//@RestController
//@RequestMapping("/replies")
//@RequiredArgsConstructor
//public class ReplyController {
//    @Tag(name = "reply")
//    @Operation(summary = "댓글 생성")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "댓글 생성을 완료 하였습니다."),
//            @ApiResponse(responseCode = "404", description = "게시글 또는 유저를 찾을 수 없습니다.")
//    })
//    @PostMapping
//    public ResponseEntity<ApplicationResponse> createReply(@RequestBody ReplyDto replyDto) {
//        return ResponseEntity.ok(ApplicationResponse.of(replyService.createReply(replyDto), HttpStatus.CREATED, "댓글 생성을 완료 하였습니다."));
//    }
//
//    @Tag(name = "reply")
//    @Operation(summary = "댓글리스트 가져오기")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "댓글 리스트 가져오기에 성공하였습니다."),
//            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.")
//    })
//    @GetMapping
//    public ResponseEntity<ApplicationResponse> getReplies(@RequestParam(name = "post-id") long postId) {
//        return ResponseEntity.ok(ApplicationResponse.of(replyService.findReplies(postId), HttpStatus.OK, "댓글 리스트 가져오기에 성공 하였습니다."));
//    }
//
//    @Tag(name = "reply")
//    @Operation(summary = "댓글 삭제")
//    @ApiResponse(responseCode = "204", description = "댓글 삭제에 성공하였습니다.")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApplicationResponse> deleteReply(@PathVariable(name = "id") long replyId) {
//        replyService.deleteReply(replyId);
//        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.NO_CONTENT, "댓글 삭제에 성공하였습니다."));
//    }
//
//    @Tag(name = "reply")
//    @Operation(summary = "댓글 수정")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "댓글 수정에 성공 하였습니다."),
//            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.")
//    })
//    @PatchMapping
//    public ResponseEntity<ApplicationResponse> updateReply(@RequestBody ReplyDto replyDto) {
//        return ResponseEntity.ok(ApplicationResponse.of(replyService.updateReply(replyDto), HttpStatus.OK, "댓글 수정에 성공 하였습니다."));
//    }
//
//    @Tag(name = "reply")
//    @Operation(summary = "댓글 좋아요",
//            description = "target-id = 좋아요를 할 댓글의 Id\n\n" +
//                    "member-id = 서버에서 준 member의 고유ID를 넣으면 됩니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "댓글 좋아요를 하였습니다."),
//            @ApiResponse(responseCode = "400", description = "댓글 좋아요 에러가 발생하였습니다.")
//    })
//    @PostMapping("/like")
//    public ResponseEntity<ApplicationResponse> likeReply(@RequestBody LikeDto likeDto) {
//        likeService.like(likeDto.getMemberId(), likeDto.getTargetId(), LikeType.REPLY);
//        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.OK, "댓글 좋아요를 하였습니다."));
//    }
//
//    @Tag(name = "reply")
//    @Operation(summary = "댓글 좋아요 취소",
//            description = "target-id = 좋아요를 할 댓글의 Id\n\n" +
//                    "member-id = 서버에서 준 member의 고유ID를 넣으면 됩니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "댓글 좋아요 취소를 하였습니다."),
//            @ApiResponse(responseCode = "400", description = "댓글 좋아요 취소 에러가 발생하였습니다.")
//    })
//    @DeleteMapping("/like")
//    public ResponseEntity<ApplicationResponse> unlikePost(@RequestParam(value = "member-id") long memberId,
//                                                  @RequestParam(value = "target-id") long targetId) {
//        likeService.unlike(memberId, targetId, LikeType.REPLY);
//        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.NO_CONTENT, "댓글 좋아요를 취소 하였습니다."));
//    }
//}