package com.Flaground.backend.module.board.controller;

import com.Flaground.backend.global.annotation.EnumFormat;
import com.Flaground.backend.global.common.ApplicationResponse;
import com.Flaground.backend.module.board.controller.dto.response.BoardInfo;
import com.Flaground.backend.module.board.controller.dto.response.BoardResponse;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "board", description = "게시판 API")
@Validated
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    
    @Tag(name = "board")
    @Operation(summary = "게시판 목록 가져오기", description = "넘어오는 값에 따라 메인 게시판과 활동 게시판 목록을 가져온다.")
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<BoardResponse> get(@RequestParam("type")
                                                  @EnumFormat(enumClass = BoardType.class) BoardType boardType) {
        List<BoardInfo> boards = boardService.get(boardType);
        return new ApplicationResponse<>(BoardResponse.of(boards, boardType));
    }
}
