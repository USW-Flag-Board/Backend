package com.FlagHome.backend.domain.search.service.impl;

import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.search.enums.SearchType;
import com.FlagHome.backend.domain.search.service.SearchService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSearchService implements SearchService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public List<PostDto> getAll() {
        return postRepository.findBoardWithCondition(null, null, null);
    }

    @Override
    @Transactional
    public List<PostDto> getWithCategory(String categoryName) {
        HashSet<String> categoriesNameSet = boardRepository.findHashSetOfCategoriesName();
        if(!categoriesNameSet.contains(categoryName))
            throw new CustomException(ErrorCode.BOARD_NOT_EXISTS);

        return postRepository.findBoardWithCondition(categoryName, null, null);
    }

    /**
    * getWithSearch가 사실상 게시판 통합검색인데, 기획의도 상 프론트 뷰 에서는 게시판별로 분류가 되서 보이는것은
     * * 현재 코드상 frontend의 책임으로 위임, 만약 프론트 단에서 처리하기 어렵다면 List<PostDto>로 주는것보다는
     * Map<String, List<PostDto>>와 같은 방식으로 줘야할듯 합니다. (2022.12.30 윤희승)
    * */
    @Override
    @Transactional
    public List<PostDto> getWithSearch(String searchCode, String searchWord) {
        return postRepository.findBoardWithCondition(null, SearchType.of(searchCode), searchWord);
    }

    @Override
    @Transactional
    public List<PostDto> getWithCategoryAndSearch(String categoryName, String searchCode, String searchWord) {
        return postRepository.findBoardWithCondition(categoryName, SearchType.of(searchCode), searchWord);
    }
}
