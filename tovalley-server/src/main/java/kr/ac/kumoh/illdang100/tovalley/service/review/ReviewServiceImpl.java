package kr.ac.kumoh.illdang100.tovalley.service.review;

import kr.ac.kumoh.illdang100.tovalley.domain.FileRootPathVO;
import kr.ac.kumoh.illdang100.tovalley.domain.ImageFile;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewImage;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final WaterPlaceRepository waterPlaceRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final TripScheduleRepository tripScheduleRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void writeReview(Long memberId, AddNewReviewReqDto addNewReviewReqDto) {

        Long tripScheduleId = addNewReviewReqDto.getTripScheduleId();
        TripSchedule findTripSchedule = findTripScheduleByIdOrElseThrowEx(tripScheduleId);
        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(findTripSchedule.getWaterPlace().getId());

        validateTripDate(findTripSchedule.getTripDate());

        validateDuplicateReview(tripScheduleId);

        Review review = createReview(findTripSchedule, addNewReviewReqDto);
        findWaterPlace.calculateRating(addNewReviewReqDto.getRating());

        saveReviewImages(review, addNewReviewReqDto.getReviewImages());
    }

    private void validateTripDate(LocalDate tripDate) {
        LocalDate now = LocalDate.now();
        if (tripDate.isAfter(now) || tripDate.isEqual(now)) {
            throw new CustomApiException("현재 날짜(" + now + ") 이후 일정에 대해 리뷰를 작성할 수 없습니다");
        }
    }

    private void validateDuplicateReview(Long tripScheduleId) {
        if (reviewRepository.existsByTripScheduleId(tripScheduleId)) {
            throw new CustomApiException("하나의 여행 일정에는 하나의 리뷰만 작성할 수 있습니다");
        }
    }

    private Review createReview(TripSchedule tripSchedule, AddNewReviewReqDto addNewReviewReqDto) {
        return reviewRepository.save(Review.builder()
                .tripSchedule(tripSchedule)
                .reviewContent(addNewReviewReqDto.getContent())
                .rating(addNewReviewReqDto.getRating())
                .waterQualityReview(addNewReviewReqDto.getWaterQuality())
                .build());
    }

    private void saveReviewImages(Review review, List<MultipartFile> reviewImages) {
        reviewImages.forEach(profileImage -> {
            try {
                ImageFile reviewImgFile = s3Service.upload(profileImage, FileRootPathVO.REVIEW_PATH);
                reviewImageRepository.save(ReviewImage.builder()
                        .review(review)
                        .imageFile(reviewImgFile)
                        .build());
            } catch (Exception e) {
                throw new CustomApiException(e.getMessage());
            }
        });
    }

    @Override
    public Slice<MyReviewRespDto> getReviewsByMemberId(Long memberId, Pageable pageable) {

        return reviewRepository.findSliceMyReviewByMemberId(memberId, pageable);
    }

    /**
     * @param waterPlaceId: 물놀이 장소 pk
     * @param pageable:     페이징 정보
     * @methodnme: getReviewsByWaterPlaceId
     * @author: JYeonJun
     * @description: 물놀이 장소 리뷰 정보 조회
     * @return: 평점, 리뷰수, 리뷰 페이징 정보
     */
    @Override
    public WaterPlaceReviewDetailRespDto getReviewsByWaterPlaceId(Long waterPlaceId, Pageable pageable) {
        WaterPlace findWaterPlace = findWaterPlaceByIdOrElseThrowEx(waterPlaceId);

        List<Review> allReviews = reviewRepository.findAllByWaterPlace_Id(waterPlaceId);

        Map<Integer, Long> ratingRatioMap = calculateRatingRatioMap(allReviews);

        Page<WaterPlaceReviewRespDto> reviewsByWaterPlaceId = reviewRepository.findReviewsByWaterPlaceId(waterPlaceId, pageable);

        double formattedRating = formatRating(findWaterPlace.getRating());

        includeReviewImages(reviewsByWaterPlaceId.getContent());

        return new WaterPlaceReviewDetailRespDto(formattedRating, findWaterPlace.getReviewCount(), ratingRatioMap, reviewsByWaterPlaceId);
    }

    private Map<Integer, Long> calculateRatingRatioMap(List<Review> reviews) {
        Map<Integer, Long> ratingRatioMap = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            ratingRatioMap.put(i, 0L);
        }

        reviews.forEach(review -> {
            int rating = review.getRating();
            ratingRatioMap.put(rating, ratingRatioMap.getOrDefault(rating, 0L) + 1);
        });

        return ratingRatioMap;
    }

    private void includeReviewImages(List<WaterPlaceReviewRespDto> reviews) {
        for (WaterPlaceReviewRespDto review : reviews) {
            Long reviewId = review.getReviewId();
            List<ReviewImage> reviewImages = reviewImageRepository.findByReview_Id(reviewId);
            List<String> reviewStoreFileUrls = reviewImages.stream()
                    .map(image -> image.getImageFile().getStoreFileUrl())
                    .collect(Collectors.toList());
            review.setReviewImages(reviewStoreFileUrls);
        }
    }

    private TripSchedule findTripScheduleByIdOrElseThrowEx(Long tripScheduleId) {
        return tripScheduleRepository.findById(tripScheduleId)
                .orElseThrow(() -> new CustomApiException("여행 일정[" + tripScheduleId + "]이 존재하지 않습니다"));
    }

    private WaterPlace findWaterPlaceByIdOrElseThrowEx(Long waterPlaceId) {
        return waterPlaceRepository.findById(waterPlaceId)
                .orElseThrow(() -> new CustomApiException("물놀이 장소[" + waterPlaceId + "]가 존재하지 않습니다"));
    }

    private double formatRating(double rating) {
        return Double.parseDouble(String.format("%.1f", rating));
    }
}