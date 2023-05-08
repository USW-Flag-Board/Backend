//package com.FlagHome.backend.module.board.service;
//
//import com.FlagHome.backend.module.board.repository.BoardRepository;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class BoardServiceTest {
//    @InjectMocks
//    private BoardService boardService;
//    @Mock
//    private BoardRepository boardRepository;
//
//    @Test
//    @DisplayName("[서비스] BOARD 추가 테스트")
//    public void createBoardTest() {
//        //given
//        Board board = Board.builder()
//                .koreanName("알고리즘")
//                .englishName("algorithm")
//                .boardDepth(0L)
//                .build();
//
//        //mocking
//        given(boardRepository.save(any())).willReturn(board);
//
//        //when
//        Board createdBoard = boardService.createBoard(board);
//
//        //then  //hamcrest
//        assertThat(createdBoard.getBoardDepth(),is(board.getBoardDepth()));
//        assertThat(createdBoard.getKoreanName(),is(board.getKoreanName()));
//        assertThat(createdBoard.getEnglishName(),is(board.getEnglishName()));
//
//    }
//
//    @Test
//    @DisplayName("[서비스] BOARD 수정 테스트")
//    public void updateBoardTest() {
//        //given
//
//        //원본카테고리 데이터가 아래라고 가정
//        Board originalBoard = Board.builder()
//                .koreanName("알고리즘")
//                .englishName("algorithm")
//                .boardDepth(1L)
//                .build();
//        originalBoard.setId(0L);
//
//        //수정데이터
//        Board board = Board.builder()
//                .koreanName("알고리즘-코테반")
//                .englishName("algorithm-codetest")
//                .build();
//        board.setId(0L);
//
//        //결과
//        Board result = Board.builder()
//                .koreanName("알고리즘-코테반")
//                .englishName("algorithm-codetest")
//                .boardDepth(1L)
//                .build();
//        result.setId(0L);
//
//        //mocking
//        given(boardRepository.findById(Mockito.any(Long.class))).willReturn(Optional.of(originalBoard));
//        given(boardRepository.save(Mockito.any(Board.class))).willReturn(result);
//        given(beanUtil.copyNotNullProperties(Mockito.any(Board.class),Mockito.any(Board.class))).willReturn(result);
//
//        //when
//        Board updatedBoard = boardService.updateBoard(board);
//
//        //then //assertJ
//        assertThat(updatedBoard.getId()).isEqualTo(result.getId());
//        assertThat(updatedBoard.getBoardDepth()).isEqualTo(result.getBoardDepth());
//        assertThat(updatedBoard.getKoreanName()).isEqualTo(result.getKoreanName());
//        assertThat(updatedBoard.getEnglishName()).isEqualTo(result.getEnglishName());
//    }
//
//    @Test
//    @DisplayName("[서비스] 전체 BOARD 읽기 테스트")
//    public void getAllBoarListTest() {
//        //given
//        //부모 자식 BOARD 관계 세팅
//        Board depth0Activity = Board.builder()
//                .boardDepth(0L).koreanName("활동").englishName("activity").build();
//
//        Board depth0Notice = Board.builder()
//                .boardDepth(0L).koreanName("공지").englishName("notice").build();
//
//        Board depth1Study = Board.builder()
//                .boardDepth(1L).koreanName("스터디").englishName("study")
//                .parent(depth0Notice)
//                .build();
//
//        Board depth2Algorithm = Board.builder()
//                .boardDepth(2L).koreanName("알고리즘-코테반").englishName("algorithm-codetest")
//                .parent(depth1Study)
//                .build();
//
//
//        List<Board> allBoardList = List.of(
//                depth0Activity, depth0Notice
//        );
//
//        //mocking
//
//        when(boardRepository.findAll()).thenReturn(allBoardList);
//
//        //when
//        List<Board> resultList = boardService.getAllBoardList();
//
//        //then 단정문
//        assert resultList.size() == 2; //depth가 0인 애들을 가져오기 떄문에 전체 데이터의 개수만 세어준다. (통합테스트에선 자식도 확인할 수 있을 것)
//    }
//
//
//    @Test
//    @DisplayName("[서비스] BOARD 삭제 테스트")
//    public void deleteBoard() {
//        //given
//        long boardId = 1L;
//        Board deleteBoard = Board.builder().build();
//        deleteBoard.setId(boardId);
//
//        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(deleteBoard)).thenReturn(null);
//
//        //when  //then
//        boardService.deleteBoard(boardId);
//
//    }
//}