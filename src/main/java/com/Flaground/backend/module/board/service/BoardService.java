package com.Flaground.backend.module.board.service;

import com.Flaground.backend.module.board.entity.Board;
import com.Flaground.backend.module.board.repository.BoardRepository;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    /**
     * Version 1
     */
    /*
    @Transactional
    public Board createBoard(Board board) {

        boardRepository.save(board);
        Board parentBoard = board.getParent();

        if(parentBoard != null)
            parentBoard.getChildren().add(board);

        return board;
    }

    @Transactional
    public List<PostDto> getPostListUsingBoardName(String boardName) {
        HashSet<String> boardsNameSet = boardRepository.findHashSetOfBoardsName();
        if(!boardsNameSet.contains(boardName))
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);

        return postRepository.findBoardWithCondition(boardName, null, null);
    }

    @Transactional
    public List<PostDto> getPostListUsingSearch(String searchCode, String searchWord) {
        return postRepository.findBoardWithCondition(null, SearchType.of(searchCode), searchWord);
    }

    @Transactional
    public List<PostDto> getPostListUsingBoardNameAndSearch(String boardName, String searchCode, String searchWord) {
        return postRepository.findBoardWithCondition(boardName, SearchType.of(searchCode), searchWord);
    }

    @Transactional
    public List<Board> getAllBoardList() {
        return boardRepository.findAll();
    }

    @Transactional
    public Board updateBoard(Board board) {

        Board findBoard = findVerifiedBoard(board.getId());

        Board updateBoard = beanUtil.copyNotNullProperties(board, findBoard);

        return boardRepository.save(updateBoard);

    }

    @Transactional
    public void deleteBoard(long boardId) {
        Board board = findVerifiedBoard(boardId);
        boardRepository.delete(board);
    }

    public Board findVerifiedBoard(long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    } */

    /**
     * Version 2
     */
    public void isExistBoard(String boardName) {
        if (isNotExist(boardName)) {
            throw new CustomException(ErrorCode.NOT_CORRECT_BOARD);
        }
    }

    public void create(Board board) {
        boardRepository.save(board);
    }

    // todo : 게시판 리스트 넘겨주기

    public void update(Board board) {
        Board findBoard = findByName(board.getName());
        findBoard.updateBoard(board);
    }

    public void delete(Board board) {
        boardRepository.delete(board);
    }

    private Board findByName(String name) {
        return boardRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_CORRECT_BOARD));
    }

    private boolean isNotExist(String boardName) {
        return !boardRepository.existsByName(boardName);
    }
}
