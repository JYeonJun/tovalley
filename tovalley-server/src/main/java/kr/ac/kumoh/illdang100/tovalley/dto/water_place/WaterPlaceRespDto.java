package kr.ac.kumoh.illdang100.tovalley.dto.water_place;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class WaterPlaceRespDto {

    @AllArgsConstructor
    @Data
    public static class RetrieveWaterPlacesDto {

        // 물놀이 장소 pk
        // 물놀이 장소 이름
        // town 정보(null일 수 있음)
        // 평점
        // 리뷰수
        // managementType 정보 (일반지역, 중점관리 지역)
        // waterPlaceCategory 정보 (일반지역, 중점관리지역)
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class WaterPlaceDetailRespDto {

        private String waterPlaceImage; // 물놀이 장소 사진
        private String waterPlaceName; // 물놀이 장소 명칭
        private String latitude; // 위도
        private String longitude; // 경도
        private String detailAddress; // 주소 + 세부지명(null)
        private String town; // 읍면(null)
        private String annualVisitors; // 연평균 총 이용객수(천명)
        private String safetyMeasures; // 안전조치 사항
        private String waterPlaceSegment; // 물놀이구간(m)
        private String dangerSegments; // 위험구역구간(null)
        private String dangerSignboardsNum; // 위험구역 설정 안내표지판(합계)
        private Double waterTemperature; // 계곡 수온(°C)
        private Double bod; // BOD(mg/L)
        private Double turbidity; // 탁도(NTU)
    }

    @AllArgsConstructor
    @Data
    public static class NationalPopularWaterPlacesDto {

        // 계곡 이미지
        private Long waterPlaceId; // 물놀이 지역 pk
        private String waterPlaceName; // 물놀이 지역 이름
        private String location; // province + city
        private String rating; // 평점
        private Integer reviewCnt; // 리뷰 개수
        private String waterPlaceImageUrl; // 물놀이 지역 이미지
    }
}
