package kr.ac.kumoh.illdang100.tovalley.service.water_place;

import kr.ac.kumoh.illdang100.tovalley.domain.Coordinate;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceReqDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.WaterPlaceDetailRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.util.EntityFinder.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterPlaceServiceImpl implements WaterPlaceService {

    private final WaterPlaceRepository waterPlaceRepository;
    private final WaterPlaceDetailRepository waterPlaceDetailRepository;
    private final RescueSupplyRepository rescueSupplyRepository;
    private final ReviewRepository reviewRepository;

    /**
     * // 물놀이 장소 리스트 조회 페이지
     * @param retrieveWaterPlacesCondition
     * @param pageable
     * @return
     */
    @Override
    public Page<RetrieveWaterPlacesDto> getWaterPlaces(RetrieveWaterPlacesCondition retrieveWaterPlacesCondition, Pageable pageable) {
        return waterPlaceRepository.findWaterPlaceList(retrieveWaterPlacesCondition, pageable);
    }

    /**
     * @methodnme: getPopularWaterPlaces
     * @author: JYeonJun
     * @description: 전국 인기 계곡 리스트(8개) 조회 메서드
     *
     * @return: 인기 계곡 내림차순(평점 or 리뷰수) 정보
     */
    @Override
    public List<NationalPopularWaterPlacesDto> getPopularWaterPlaces(String cond) {
        List<WaterPlace> waterPlaces;

        Pageable pageable = PageRequest.of(0, 8);

        if ("RATING".equals(cond)) {
            waterPlaces = waterPlaceRepository.findTop8ByOrderByRatingDesc(pageable);
        } else {
            waterPlaces = waterPlaceRepository.findTop8ByOrderByReviewCountDesc(pageable);
        }

        return waterPlaces.stream()
                .map(this::mapToPopularWaterPlaceDto)
                .collect(Collectors.toList());
    }

    private NationalPopularWaterPlacesDto mapToPopularWaterPlaceDto(WaterPlace wp) {
        String location = wp.getProvince() + " " + wp.getCity();
        int reviewCount = wp.getReviewCount();
        Double rating = wp.getRating();
        String formattedRating = getFormattedRating(rating);
        String waterPlaceImageUrl = wp.getWaterPlaceImageUrl();

        return new NationalPopularWaterPlacesDto(wp.getId(), wp.getWaterPlaceName(), location, formattedRating, reviewCount, waterPlaceImageUrl);
    }

    private String getFormattedRating(Double rating) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(rating);
    }

    /**
     * @methodnme: getWaterPlaceDetailByWaterPlace
     * @author: JYeonJun
     * @param waterPlaceId: 물놀이 장소 pk
     * @description: 물놀이 장소 상세정보 조회
     * @return: 물놀이 장소 상세정보
     */
    @Override
    public WaterPlaceDetailRespDto getWaterPlaceDetailByWaterPlace(Long waterPlaceId) {
        WaterPlaceDetail findWaterPlaceDetail =
                findWaterPlaceDetailByWaterPlaceIdOrElseThrowEx(waterPlaceDetailRepository, waterPlaceId);

        List<Review> findReviews = reviewRepository.findAllByWaterPlace_Id(waterPlaceId);
        Map<String, Integer> reviewCounts = countReviewOccurrences(findReviews);

        WaterPlace findWaterPlace = findWaterPlaceDetail.getWaterPlace();
        Coordinate coordinate = findWaterPlace.getCoordinate();

        return createWaterPlaceDetailRespDto(findWaterPlace, coordinate, findWaterPlaceDetail, reviewCounts);
    }

    private Map<String, Integer> countReviewOccurrences(List<Review> reviews) {
        Map<String, Integer> reviewCounts = new HashMap<>();

        for (Review review : reviews) {
            WaterQualityReviewEnum reviewEnum = review.getWaterQualityReview();

            reviewCounts.put(reviewEnum.getValue(), reviewCounts.getOrDefault(reviewEnum.getValue(), 0) + 1);
        }

        return reviewCounts;
    }

    /**
     * @methodnme: getRescueSuppliesByWaterPlace
     * @author: JYeonJun
     * @param waterPlaceId: 물놀이 장소 pk
     * @description: 물놀이 장소에 배치된 구조용품 현황 조회
     * @return: 구조용품 수량
     */
    @Override
    public RescueSupplyByWaterPlaceRespDto getRescueSuppliesByWaterPlace(Long waterPlaceId) {

        RescueSupply findRescueSupply = findRescueSupplyByWaterPlaceIdOrElseThrowEx(rescueSupplyRepository, waterPlaceId);

        return RescueSupplyByWaterPlaceRespDto.createRescueSupplyRespDto(findRescueSupply);
    }
}
