package com.Flaground.backend.module.board.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.board.controller.dto.response.BoardInfo;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<BoardInfo> get(BoardType boardType) {
        return boardRepository.getBoards(boardType);
    }

    @Transactional(readOnly = true)
    public void validateBoard(String boardName) {
        if (!isExist(boardName)) {
            throw new CustomException(ErrorCode.NOT_CORRECT_BOARD);
        }
    }

    public void create(Board board) {
        isDuplicateName(board.getName());
        boardRepository.save(board);
    }

    public void update(String boardName, Board board) {
        Board findBoard = findByName(boardName);
        isDuplicateName(board.getName());
        findBoard.updateBoard(board);
    }

    // todo : 삭제된 게시판의 게시글들 옮겨주기
    public void delete(String boardName) {
        Board board = findByName(boardName);
        boardRepository.delete(board);
    }

    private Board findByName(String name) {
        return boardRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_CORRECT_BOARD));
    }

    private void isDuplicateName(String boardName) {
        if (isExist(boardName)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_BOARD);
        }
    }

    private boolean isExist(String boardName) {
        return boardRepository.existsByName(boardName);
    }
}
