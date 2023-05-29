package com.Flaground.backend.module.board;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.module.board.controller.dto.response.BoardInfo;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.domain.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardRepositoryTest extends RepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void 게시판_목록_가져오기_테스트() {
        // given
        final String boardName = "자유게시판";
        final BoardType boardType = BoardType.MAIN;

        Board board = boardRepository.save(Board.builder().name(boardName).boardType(boardType).build());

        // when
        List<BoardInfo> boards = boardRepository.getBoards(boardType);

        // then
        assertThat(boards.get(0).getId()).isEqualTo(board.getId());
        assertThat(boards.get(0).getBoardName()).isEqualTo(boardName);
    }
}
