package com.FlagHome.backend.v1.reply.controller;

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

    @GetMapping("/get")
    public ResponseEntity<List<Reply>> getReplies(@RequestParam(name = "id") long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(replyService.findReplies(postId));
    }
}