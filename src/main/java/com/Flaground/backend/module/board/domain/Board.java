package com.Flaground.backend.module.board.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Builder
    public Board(String name, BoardType boardType) {
        this.name = name;
        this.boardType = boardType;
    }

    public void updateBoard(Board board) {
        this.name = board.getName();
        this.boardType = board.getBoardType();
    }
}