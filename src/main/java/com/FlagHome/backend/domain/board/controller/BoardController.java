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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "board", description = "게시판 API")
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardMapper mapper;
    private final static String BOARD_DEFAULT_URL = "/api/board";

    @Tag(name = "board")
    @Operation(summary = "게시판 생성")
    @ApiResponse(responseCode = "201", description = "게시판이 생성 되었습니다.")
    @PostMapping
    public ResponseEntity<Void> createBoard(@RequestBody BoardPostDto boardPostDto) {
        Board resultBoard = boardService.createBoard(mapper.BoardPostDtoToBoard(boardPostDto, boardService));
        URI location = UriCreator.createUri(BOARD_DEFAULT_URL, resultBoard.getId());

        return ResponseEntity.created(location).build();
    }

    @Tag(name = "board")
    @Operation(summary = "게시판 가져오기",
            description = "Get Request Query에 따라 할수 있는 사항은 아래와 같습니다.\n" +
                            "1. 특정 게시판의 게시글 리스트 가져오기 (query를 name만 사용하세요.)\n" +
                            "2. 특정 게시판에서 검색키워드 적용하여 게시글 리스트 가져오기 (query를 name, search-code, search-word를 사용하세요.)\n" +
                            "3. 게시판 상관없이 검색키워드만 적용하여 게시글 리스트 가져오기 (query를 search-code, search-word만 사용하세요.\n" +
                            "\n[ 현재 지원하는 search-code는 title, content, title-and-content, user-name 입니다. ]")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 리스트 가져오기에 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판이거나, 올바른 검색키워드가 아닙니다.")
    })
    @GetMapping
    public ResponseEntity<List<PostDto>> getBoard(@RequestParam(value = "name", required = false) String boardName,
                                  @RequestParam(value = "search-code", required = false) String searchCode,
                                  @RequestParam(value = "search-word", required = false) String searchWord) {
        if(boardName != null && searchCode != null && searchWord != null)
            return ResponseEntity.status(HttpStatus.OK).body(boardService.getPostListUsingBoardNameAndSearch(boardName, searchCode, searchWord));
        else if(boardName != null && (searchCode == null || searchWord == null))
            return ResponseEntity.status(HttpStatus.OK).body(boardService.getPostListUsingBoardName(boardName));
        else if(boardName == null && (searchCode != null && searchWord != null))
            return ResponseEntity.status(HttpStatus.OK).body(boardService.getPostListUsingSearch(searchCode, searchWord));
        else
            throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    @Tag(name = "board")
    @Operation(summary = "게시판 리스트 가져오기")
    @ApiResponse(responseCode = "200", description = "모든 게시판의 리스트를 가져옵니다.")
    @GetMapping("/list")
    public ResponseEntity<?> getAllBoardList() {
        return ResponseEntity.ok(mapper.BoardListToBoardResultDtoList(boardService.getAllBoardList()));
    }

    @Tag(name = "board")
    @Operation(summary = "게시판 수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 수정을 성공하였습니다."),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 게시판 입니다.")
    })
    @PatchMapping("/{board-id}")
    public ResponseEntity<Void> updateBoard(@PathVariable("board-id") long boardId,
                                         @RequestBody BoardPatchDto boardPatchDto) {
        boardPatchDto.setId(boardId);
        boardService.updateBoard(mapper.BoardPatchDtoToBoard(boardPatchDto, boardService));
        return ResponseEntity.ok().build();
    }

    @Tag(name = "board")
    @Operation(summary = "게시판 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시판 삭제를 완료하였습니다."),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 게시판 입니다.")
    })
    @DeleteMapping("/{board-id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("board-id") long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}
