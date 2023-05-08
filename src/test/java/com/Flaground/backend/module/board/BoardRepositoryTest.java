package com.Flaground.backend.module.board;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.module.board.controller.dto.request.BoardRequest;
import com.Flaground.backend.module.board.controller.dto.response.BoardInfo;
import com.Flaground.backend.module.board.controller.mapper.BoardMapper;
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

    @Autowired
    private BoardMapper boardMapper;

    private BoardRequest request;

    @Test
    public void 게시판_목록_가져오기_테스트() {
        // given
        final String boardName = "자유게시판";
        final BoardType boardType = BoardType.main;
        initRequest(boardName, boardType);
        Board board = boardRepository.save(boardMapper.mapFrom(request));

        // when
        List<BoardInfo> boards = boardRepository.getBoards(boardType);

        // then
        assertThat(boards.get(0).getId()).isEqualTo(board.getId());
        assertThat(boards.get(0).getBoardName()).isEqualTo(boardName);
    }

    private void initRequest(String name, BoardType boardType) {
        request = BoardRequest.builder()
                .name(name)
                .boardType(boardType)
                .build();
    }
}
