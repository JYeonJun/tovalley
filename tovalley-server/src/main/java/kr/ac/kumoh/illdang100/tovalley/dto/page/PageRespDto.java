package kr.ac.kumoh.illdang100.tovalley.dto.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.ac.kumoh.illdang100.tovalley.dto.member.MemberRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static kr.ac.kumoh.illdang100.tovalley.dto.accident.AccidentRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.rescue_supply.RescueSupplyRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.water_place.WaterPlaceRespDto.*;
import static kr.ac.kumoh.illdang100.tovalley.dto.weather.WeatherRespDto.*;

public class PageRespDto {

    @AllArgsConstructor
    @Data
    public static class MainPageAllRespDto {

        private List<NationalWeatherRespDto> nationalWeather; // 전국 지역 날씨 (5일)
        private AlertRespDto weatherAlert; // 기상 특보 정보
        private AccidentCountDto accidentCountDto; // 전국 지역 올해 사고 발생수
        private List<NationalPopularWaterPlacesDto> nationalPopularWaterPlaces; // 전국 인기 물놀이 지역 리스트
    }

    @AllArgsConstructor
    @Data
    public static class WaterPlaceDetailPageAllRespDto {

        private List<DailyWaterPlaceWeatherDto> waterPlaceWeathers; // 물놀이 장소 날씨 (5일)
        private WaterPlaceDetailRespDto waterPlaceDetails; // 물놀이 장소 정보
        private RescueSupplyByWaterPlaceRespDto rescueSupplies; // 구조용품 현황
        private WaterPlaceAccidentFor5YearsDto accidents; // 최근 5년간 사고 수
        private Map<LocalDate, Integer> tripPlanToWaterPlace; // 여행 계획
        private WaterPlaceReviewDetailRespDto reviewRespDto; // 리뷰
    }

    @AllArgsConstructor
    @Data
    public static class MyPageAllRespDto {

        private MemberRespDto.MemberProfileRespDto userProfile; // 개인정보
        private List<MyReviewRespDto> myReviews;
    }


}
