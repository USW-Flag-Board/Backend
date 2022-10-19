package com.FlagHome.backend.v1.reply.service;

import com.FlagHome.backend.v1.reply.entity.Reply;
import com.FlagHome.backend.v1.reply.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    public List<Reply> findReplies(Long postId) {
        // 아직 미구현 아래는 테스트 코드, 해당주석은 삭제예정 입니다. (2022.10.19 윤희승)
        List<Reply> foundReplies = new ArrayList<>();
        return foundReplies;
    }
}