package com.FlagHome.backend.domain.board.repository;

import com.FlagHome.backend.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryCustom {

}
