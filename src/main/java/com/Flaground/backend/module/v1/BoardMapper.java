//package com.Flaground.backend.module.board.controller.mapper;
//
//import org.mapstruct.Mapper;
//import org.mapstruct.ReportingPolicy;
//
//@Mapper(
//        componentModel = "spring",
//        unmappedTargetPolicy = ReportingPolicy.WARN
//)
//public interface BoardMapper {
//    /**
//     * Version 1
//     */
//    /* BoardResultDto BoardToBoardResultDto(Board board);
//    List<BoardResultDto> BoardListToBoardResultDtoList(List<Board> categories);
//
//    default Board BoardPostDtoToBoard(BoardPostDto boardPostDto, BoardService boardService) {
//
//        if ( boardPostDto == null ) {
//            return null;
//        }
//
//        Board parentBoard = null;
//
//        if(boardPostDto.getBoardDepth() !=0){
//            parentBoard = boardService.findVerifiedBoard( boardPostDto.getParentId());
//        }
//
//
//        Board board = Board.builder()
//                .koreanName(boardPostDto.getKoreanName())
//                .englishName(boardPostDto.getEnglishName())
//                .boardDepth(boardPostDto.getBoardDepth())
//                .parent(parentBoard)
//                .build();
//
//        return board;
//    }
//    default Board BoardPatchDtoToBoard(BoardPatchDto boardPatchDto, BoardService boardService) {
//        if (boardPatchDto == null) {
//            return null;
//        }
//
//        Board parentBoard = null;
//        if(boardPatchDto.getBoardDepth() != null){ //수정이므로, depth는 Null일 수 있음
//            if(boardPatchDto.getBoardDepth() !=0){ //depth가 0이면 부모가 없음 > 0이 아니면 부모의 정보를 가져옴
//                parentBoard = boardService.findVerifiedBoard(boardPatchDto.getParentId());
//            }
//        }
//        Board board = Board.builder()
//                .koreanName(boardPatchDto.getKoreanName())
//                .englishName(boardPatchDto.getEnglishName())
//                .boardDepth(boardPatchDto.getBoardDepth())
//                .parent(parentBoard)
//                .build();
//
//        board.setId(boardPatchDto.getId());
//
//        return board;
//    } */
//    /**
//     * Version 2
//     */
//}
