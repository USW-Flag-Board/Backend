package com.Flaground.backend.module.board.controller;

import com.Flaground.backend.module.board.controller.dto.BoardRequest;
import com.Flaground.backend.module.board.mapper.BoardMapper;
import com.Flaground.backend.module.board.service.BoardService;
import com.Flaground.backend.global.common.ApplicationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "board", description = "게시판 API")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final static String DEFAULT_URL = "/boards";
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    /**
     * Version 1
     */
    /*@Tag(name = "board")
    @Operation(summary = "게시판 생성")
    @ApiResponse(responseCode = "201", description = "게시판이 생성 되었습니다.")
    @PostMapping
    public ResponseEntity<Void> createBoard(@RequestBody BoardPostDto boardPostDto) {
        Board resultBoard = boardService.createBoard(mapper.BoardPostDtoToBoard(boardPostDto, boardService));
        URI location = UriCreator.createURI(BOARD_DEFAULT_URL, resultBoard.getId());

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
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@PathVariable("id") long boardId,
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("id") long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    } */

    /**
     * Version 2
     */
    @Tag(name = "board")
    @ResponseStatus(CREATED)
    @PostMapping
    public ApplicationResponse create(@RequestBody @Valid BoardRequest boardRequest) {
        boardService.create(boardMapper.mapFrom(boardRequest));
        return new ApplicationResponse<>();
    }

    @Tag(name = "board")
    @ResponseStatus(OK)
    @PutMapping
    public ApplicationResponse update(@RequestBody @Valid BoardRequest boardRequest) {
        boardService.update(boardMapper.mapFrom(boardRequest));
        return new ApplicationResponse<>();
    }

//    @ResponseStatus(OK)
//    @DeleteMapping
//    public ApplicationResponse delete() {
//        return new ApplicationResponse<>();
//    }
}
