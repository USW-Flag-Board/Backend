package com.FlagHome.backend.domain.reply;

import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import com.FlagHome.backend.global.config.QueryDslConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(QueryDslConfig.class)
public class ReplyRepositoryTest {
    @Autowired
    private ReplyRepository replyRepository;
}
