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
    /**
     * Version 1
     */

    /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column
    private String koreanName;

    @Column
    private String englishName;

    @Column
    private Long boardDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent")
    private Board parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private final List<Board> children = new ArrayList<>();

    @Builder
    public Board(String koreanName, String englishName, Long boardDepth, Board parent) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.boardDepth = boardDepth;
        this.parent = parent;
    } */

    /**
     * Version 2
     */
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