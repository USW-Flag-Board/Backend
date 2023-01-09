package com.FlagHome.backend.domain.board.controller;

import com.FlagHome.backend.domain.board.dto.BoardPatchDto;
import com.FlagHome.backend.domain.board.dto.BoardPostDto;
import com.FlagHome.backend.domain.board.dto.BoardResultDto;
import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.mapper.BoardMapper;
import com.FlagHome.backend.domain.board.service.BoardService;
import com.FlagHome.backend.global.util.UriCreator;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper mapper;
    private final static String BOARD_DEFAULT_URL = "/api/board";

    @PostMapping
    public ResponseEntity createBoard(@RequestBody BoardPostDto boardPostDto) {
        Board resultBoard = boardService.createBoard(mapper.BoardPostDtoToBoard(boardPostDto, boardService));
        URI location = UriCreator.createUri(BOARD_DEFAULT_URL, resultBoard.getId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{board-id}")
    public ResponseEntity updateBoard(@PathVariable("board-id") long boardId,
                                         @RequestBody BoardPatchDto boardPatchDto) {
        boardPatchDto.setId(boardId);
        Board board = mapper.BoardPatchDtoToBoard(boardPatchDto, boardService);
        boardService.updateBoard(board);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<?> getBoardList() {
        List<BoardResultDto> result =  mapper.BoardListToBoardResultDtoList(boardService.getBoardList());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}
