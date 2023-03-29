package com.FlagHome.backend.domain.post.controller.dto;

public class PostDtoV1 {
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @EqualsAndHashCode
//    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
//    public class GetPostResponse {
//        private long id;
//        private long userId;
//        private String title;
//        private String content;
//        private String imgUrl;
//        private String fileUrl;
//        private List<ReplyDto> replyList;
//        private long boardId;
//        private Status status;
//        private long viewCount;
//        private long likeCount;
//        private LocalDateTime createdAt;
//        private LocalDateTime updatedAt;
//
//        public GetPostResponse(Post postEntity) {
//            this.id = postEntity.getId();
//            this.userId = postEntity.getMember().getId();
//            this.title = postEntity.getTitle();
//            this.content = postEntity.getContent();
//            this.imgUrl = postEntity.getImgUrl();
//            this.fileUrl = postEntity.getFileUrl();
//            this.boardId = postEntity.getBoard().getId();
//            this.status = postEntity.getStatus();
//            this.viewCount = postEntity.getViewCount();
//            this.createdAt = postEntity.getCreatedAt();
//            //this.likeCount = postEntity.getLikeList();
//
//            replyList = new ArrayList<>();
//            for(Reply reply : postEntity.getReplyList())
//                replyList.add(new ReplyDto(reply));
//        }
//    }
//
//    @Getter
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    @AllArgsConstructor
//    public class LightPostDto {
//        private Long id;
//        private String title;
//        private String boardName;
//        private LocalDateTime createdAt;
//        private Long viewCount;
//        private Integer likeCount;
//    }
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @EqualsAndHashCode
//    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
//    public class PostDto {
//        private long id;
//        private long userId;
//        private String title;
//        private String content;
//        private String imgUrl;
//        private String fileUrl;
//        private String memberName;
//        private List<ReplyDto> replyList;
//        private List<LikeDto> likeList;
//        private int replyCount;
//        private int likeCount;
//        private long boardId;
//        private Status status;
//        private long viewCount;
//        private LocalDateTime createdAt;
//        private LocalDateTime updatedAt;
//
//        public PostDto(Post postEntity) {
//            this.id = postEntity.getId();
//            this.userId = postEntity.getMember().getId();
//            this.title = postEntity.getTitle();
//            this.content = postEntity.getContent();
//            this.imgUrl = postEntity.getImgUrl();
//            this.fileUrl = postEntity.getFileUrl();
//            this.boardId = postEntity.getBoard().getId();
//            this.status = postEntity.getStatus();
//            this.viewCount = postEntity.getViewCount();
//
//            this.replyList = new ArrayList<>();
//            for(Reply reply : postEntity.getReplyList())
//                this.replyList.add(new ReplyDto(reply));
//
//            this.likeList = new ArrayList<>();
//            for(Like eachLike : postEntity.getLikeList())
//                this.likeList.add(new LikeDto(eachLike.getMember().getId(), eachLike.getTargetId()));
//        }
//
//        // Projection용 생성자
//        public PostDto(long id, String title, LocalDateTime createdAt, long boardId, String memberName, long viewCount, int likeCount, int replyCount) {
//            this.id = id;
//            this.title = title;
//            this.createdAt = createdAt;
//            this.boardId = boardId;
//            this.memberName = memberName;
//            this.viewCount = viewCount;
//            this.likeCount = likeCount;
//            this.replyCount = replyCount;
//        }
}
