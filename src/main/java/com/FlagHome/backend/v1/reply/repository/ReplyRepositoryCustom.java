package com.FlagHome.backend.v1.reply.repository;

import com.FlagHome.backend.v1.reply.entity.Reply;

import java.util.List;

public interface ReplyRepositoryCustom {
    List<Reply> findByPostId(long postId);
}
