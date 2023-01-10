package com.FlagHome.backend.domain.board.service;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    @Transactional
    public Board createBoard(Board board){

        boardRepository.save(board);
        Board parentBoard = board.getParent();

        if(parentBoard != null)
            parentBoard.getChildren().add(board);

        return board;
    }
    @Transactional
    public Board updateBoard(Board board) {

        Board findBoard = findVerifiedBoard(board.getId());

        Optional.ofNullable(board.getKoreanName())
                .ifPresent(koreanName -> findBoard.setKoreanName(koreanName));
        Optional.ofNullable(board.getEnglishName())
                .ifPresent(englishName -> findBoard.setEnglishName(englishName));
        Optional.ofNullable(board.getBoardDepth())
                .ifPresent(depth -> findBoard.setBoardDepth(depth));
        Optional.ofNullable(board.getParent())
                .ifPresent(parent -> findBoard.setParent(
                        findVerifiedBoard(board.getParent().getId())
                ));

        return boardRepository.save(findBoard);

    }

    @Transactional
    public List<Board> getAllBoardList() {
        return boardRepository.findAll();
    }

    @Transactional
    public void deleteBoard(long boardId) {
        Board board = findVerifiedBoard(boardId);
        boardRepository.delete(board);
    }

    public Board findVerifiedBoard(long boardId) {
        Optional<Board> optionalCategory = boardRepository.findById(boardId);
        Board findBoard = optionalCategory.orElseThrow(()-> new CustomException(ErrorCode.BOARD_NOT_EXISTS));
        return findBoard;
    }
}
