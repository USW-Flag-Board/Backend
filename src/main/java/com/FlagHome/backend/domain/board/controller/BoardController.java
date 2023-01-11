package com.FlagHome.backend.domain.board.controller;

import com.FlagHome.backend.domain.board.dto.BoardPatchDto;
import com.FlagHome.backend.domain.board.dto.BoardPostDto;
import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.mapper.BoardMapper;
import com.FlagHome.backend.domain.board.service.BoardService;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.UriCreator;
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
    public ResponseEntity<Void> createBoard(@RequestBody BoardPostDto boardPostDto) {
        Board resultBoard = boardService.createBoard(mapper.BoardPostDtoToBoard(boardPostDto, boardService));
        URI location = UriCreator.createUri(BOARD_DEFAULT_URL, resultBoard.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public List<PostDto> getBoard(@RequestParam(value = "name", required = false) String boardName,
                                  @RequestParam(value = "search-code", required = false) String searchCode,
                                  @RequestParam(value = "search-word", required = false) String searchWord) {
        if(boardName != null && searchCode != null && searchWord != null)
            return boardService.getPostListUsingBoardNameAndSearch(boardName, searchCode, searchWord);
        else if(boardName != null && (searchCode == null || searchWord == null))
            return boardService.getPostListUsingBoardName(boardName);
        else if(boardName == null && (searchCode != null && searchWord != null))
            return boardService.getPostListUsingSearch(searchCode, searchWord);
        else
            throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllBoardList() {
        return ResponseEntity.ok(mapper.BoardListToBoardResultDtoList(boardService.getAllBoardList()));
    }

    @PatchMapping("/{board-id}")
    public ResponseEntity<Void> updateBoard(@PathVariable("board-id") long boardId,
                                         @RequestBody BoardPatchDto boardPatchDto) {
        boardPatchDto.setId(boardId);
        boardService.updateBoard(mapper.BoardPatchDtoToBoard(boardPatchDto, boardService));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{board-id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("board-id") long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}
