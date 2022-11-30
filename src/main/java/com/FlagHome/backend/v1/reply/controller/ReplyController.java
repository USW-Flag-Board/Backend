package com.FlagHome.backend.v1.reply.controller;

import com.FlagHome.backend.v1.reply.dto.ReplyDto;
import com.FlagHome.backend.v1.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    @PostMapping("/create")
    public ResponseEntity<ReplyDto> createReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.createReply(replyDto));
    }

    @GetMapping("/get")
    public ResponseEntity<List<ReplyDto>> getReplies(@RequestParam(name = "id") long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.findReplies(postId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable(name = "id") long replyId) {
        replyService.deleteReply(replyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/modify")
    public ResponseEntity<ReplyDto> updateReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.updateReply(replyDto));
    }
}