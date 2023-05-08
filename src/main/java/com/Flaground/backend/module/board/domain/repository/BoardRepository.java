package com.Flaground.backend.module.board.domain.repository;

import com.Flaground.backend.module.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryCustom {
    Optional<Board> findByName(String name);

    boolean existsByName(String boardName);
}
