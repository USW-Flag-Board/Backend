package com.FlagHome.backend.common;

import com.FlagHome.backend.global.config.QueryDslConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(QueryDslConfig.class)
public class RepositoryTest {
}
