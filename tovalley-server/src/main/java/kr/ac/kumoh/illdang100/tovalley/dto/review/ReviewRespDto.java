package kr.ac.kumoh.illdang100.tovalley.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReviewRespDto {

    @AllArgsConstructor
    @Data
    public static class WaterPlaceReviewRespDto {

        private Long reviewId;
        private String memberProfileImg;
        private String nickname;
        private Integer rating;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdReviewDate;
        private String content;
        private List<String> reviewImages;
        private String waterQuality;
        @JsonProperty("isMyReview")
        private boolean isMyReview;

        public WaterPlaceReviewRespDto(Long reviewId, String memberProfileImg, String nickname, Integer rating,
                                       LocalDateTime createdReviewDate, String content, WaterQualityReviewEnum waterQuality,
                                       boolean isMyReview) {
            this.reviewId = reviewId;
            this.memberProfileImg = memberProfileImg;
            this.nickname = nickname;
            this.rating = rating;
            this.createdReviewDate = createdReviewDate;
            this.content = content;
            this.waterQuality = waterQuality.getValue();
            this.isMyReview = isMyReview;
        }
    }

    @AllArgsConstructor
    @Data
    public static class MyReviewRespDto {

        private Long reviewId;
        private Long waterPlaceId;
        private String waterPlaceName;
        private Integer rating;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdReviewDate;
        private String content;
        private List<String> reviewImages;
        private String waterQuality;

        public MyReviewRespDto(Long reviewId, Long waterPlaceId, String waterPlaceName, Integer rating, LocalDateTime createdReviewDate, String content,
                                WaterQualityReviewEnum waterQuality) {
            this.reviewId = reviewId;
            this.waterPlaceId = waterPlaceId;
            this.waterPlaceName = waterPlaceName;
            this.rating = rating;
            this.createdReviewDate = createdReviewDate;
            this.content = content;
            this.waterQuality = waterQuality.getValue();
        }
    }

    @AllArgsConstructor
    @Data
    public static class WaterPlaceReviewDetailRespDto {
        private Double waterPlaceRating; // 물놀이 장소 평점
        private int reviewCnt; // 리뷰 수
        private Map<Integer, Long> ratingRatio;
        private Page<WaterPlaceReviewRespDto> reviews; // 리뷰
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class RecentReviewRespDto {
        private Long reviewId;
        private int reviewRating;
        private String reviewContent;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime reviewCreatedAt;
        private Long waterPlaceId;

        public static RecentReviewRespDto createRecentReviewRespDto(Review review) {
            return RecentReviewRespDto.builder()
                    .reviewId(review.getId())
                    .reviewContent(review.getReviewContent())
                    .reviewRating(review.getRating())
                    .reviewCreatedAt(review.getCreatedDate())
                    .waterPlaceId(review.getTripSchedule().getWaterPlace().getId())
                    .build();
        }
    }
}
