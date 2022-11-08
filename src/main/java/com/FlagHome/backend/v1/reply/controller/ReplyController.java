package com.FlagHome.backend.v1.reply.controller;

import com.FlagHome.backend.v1.reply.dto.ReplyDto;
import com.FlagHome.backend.v1.reply.entity.Reply;
import com.FlagHome.backend.v1.reply.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @PostMapping("/create")
    public ResponseEntity<ReplyDto> createReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.createReply(replyDto));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Reply>> getReplies(@RequestParam(name = "id") long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.findReplies(postId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteReply(@PathVariable(name = "id") long replyId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(replyService.deleteReply(replyId));
    }

    @PutMapping("/modify")
    public ResponseEntity<ReplyDto> updateReply(@RequestBody ReplyDto replyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.updateReply(replyDto));
    }
}