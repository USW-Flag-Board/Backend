//package com.Flaground.backend.module.board.controller.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.util.List;
//
//public class BoardDtoV1 {
//    @Getter
//    @AllArgsConstructor
//    public class BoardPatchDto {
//        private Long id;
//        private Long parentId;
//        private String koreanName;
//        private String englishName;
//        private Long boardDepth;
//    }
//
//    @Getter
//    @AllArgsConstructor
//    public class BoardPostDto {
//        private Long parentId;
//        private String koreanName;
//        private String englishName;
//        private Long boardDepth;
//    }
//
//    @Getter
//    @AllArgsConstructor
//    public class BoardResultDto {
//        private String koreanName;
//        private String englishName;
//        private Long boardDepth;
//        private List<BoardResultDto> children;
//    }
//}
