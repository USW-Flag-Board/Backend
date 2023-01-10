package com.FlagHome.backend.domain.board.entity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class BoardTest {

    public Board activityBoardDepth0() {
        return Board.builder()
                .koreanName("활동게시판")
                .englishName("Activity")
                .boardDepth(0L)
                .postList(new ArrayList<>())
                .build();
    }
    public Board algorithmBoardDepth1() {

        return Board.builder()
                .koreanName("알고리즘")
                .englishName("Activity")
                .boardDepth(1L)
                .postList(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("[도메인 테스트] BOARD 생성")
    void createBoardTest(){
        //given
        Board board = activityBoardDepth0();

        //when, then
        Assertions.assertThat(board.getKoreanName()).isEqualTo("활동게시판") ;
        Assertions.assertThat(board.getEnglishName()).isEqualTo("Activity");
        Assertions.assertThat(board.getPostList().size()).isEqualTo(0) ;
        Assertions.assertThat(board.getParent()).isEqualTo(null);
        Assertions.assertThat(board.getBoardDepth()).isEqualTo(0L);
    }

    @Test
    @DisplayName("[도메인 테스트] BOARD 데이터 수정")
    void updateBoardTest(){
        //given
        Board board = algorithmBoardDepth1();
        board.setParent(activityBoardDepth0());

        //when
        board.setKoreanName("알고리즘-코테반");
        board.setEnglishName("algorithm-codetest");

        //then
        Assertions.assertThat(board.getKoreanName()).isEqualTo("알고리즘-코테반");
        Assertions.assertThat(board.getEnglishName()).isEqualTo("algorithm-codetest");
    }

    @Test
    @DisplayName("[도메인 테스트] BOARD 수정 : 부모 BOARD 변경")
    void updateBoardChangeParent(){
        //given
        Board algorithm = algorithmBoardDepth1();
        algorithm.setParent(activityBoardDepth0()); //원래 부모

        Assertions.assertThat(algorithm.getParent().getKoreanName()).isEqualTo("활동게시판");
        Assertions.assertThat(algorithm.getParent().getEnglishName()).isEqualTo("Activity");

        //새 부모 생성
        Board study =   Board.builder()
                .boardDepth(0L)
                .koreanName("스터디")
                .englishName("Study")
                .postList(new ArrayList<>())
                .build();

        //when
        algorithm.setParent(study);

        //then
        Assertions.assertThat(algorithm.getParent().getKoreanName()).isEqualTo("스터디");
        Assertions.assertThat(algorithm.getParent().getEnglishName()).isEqualTo("Study");
        Assertions.assertThat(algorithm.getParent().getBoardDepth()).isEqualTo(algorithm.getBoardDepth() - 1);
    }

}