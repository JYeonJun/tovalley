package kr.ac.kumoh.illdang100.tovalley.service.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

/**
 * 리뷰 관리
 */
public interface ReviewService {

    // 리뷰 작성
    // 리뷰 작성 후 리뷰 이미지의 fk(reviewId) 등록하기
    void writeReview(Long memberId, AddNewReviewReqDto addNewReviewReqDto);

    // 특정 회원의 리뷰 조회
    Slice<MyReviewRespDto> getReviewsByMemberId(Long memberId, Pageable pageable);

    // 특정 물놀이 장소의 리뷰 조회
    WaterPlaceReviewDetailRespDto getReviewsByWaterPlaceId(Long waterPlaceId, Long memberId, Pageable pageable);

    List<RecentReviewRespDto> getRecentReviewTop3();
}
