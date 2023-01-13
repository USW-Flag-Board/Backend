package com.FlagHome.backend.domain.board.service;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CustomBeanUtils beanUtil;
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

        Board updateBoard = (Board) beanUtil.copyNotNullProperties(board, findBoard);

        return boardRepository.save(updateBoard);

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
