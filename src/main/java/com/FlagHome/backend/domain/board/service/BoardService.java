package com.FlagHome.backend.domain.board.service;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.enums.SearchType;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final CustomBeanUtils<Board> beanUtil;

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
            throw new CustomException(ErrorCode.BOARD_NOT_EXISTS);

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
        Optional<Board> optionalCategory = boardRepository.findById(boardId);
        Board findBoard = optionalCategory.orElseThrow(()-> new CustomException(ErrorCode.BOARD_NOT_EXISTS));
        return findBoard;
    }
}
