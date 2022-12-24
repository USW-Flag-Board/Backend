package com.FlagHome.backend.domain.reply.controller;

import com.FlagHome.backend.domain.reply.dto.ReplyDto;
import com.FlagHome.backend.domain.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<ReplyDto> createReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.createReply(replyDto));
    }

    @GetMapping
    public ResponseEntity<List<ReplyDto>> getReplies(@RequestParam(name = "id") long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.findReplies(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable(name = "id") long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<ReplyDto> updateReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.updateReply(replyDto));
    }
}