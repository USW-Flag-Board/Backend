package com.FlagHome.backend.v1.category.repository;

import com.FlagHome.backend.v1.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepository {
    private final EntityManager em; //엔티티 매니저를 가져오는게 문제가 됨 >> 비추천
    //JPA or JPQL or QueryBuilder (QueryDsl)을 이용해 개선이 필요함
    public List<Category> findAll(){
        return em.createQuery(
                        "select c " +
                                "from Category c " +
                                "where c.parent is NULL"
                        ,Category.class)
                .getResultList();
    }
}
