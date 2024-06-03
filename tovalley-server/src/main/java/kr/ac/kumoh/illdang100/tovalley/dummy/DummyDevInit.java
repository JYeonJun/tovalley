package kr.ac.kumoh.illdang100.tovalley.dummy;

import kr.ac.kumoh.illdang100.tovalley.domain.accident.Accident;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.accident.AccidentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.Review;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewImageRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.ReviewRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.review.WaterQualityReviewEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripSchedule;
import kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.TripScheduleRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.service.OpenApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Configuration
@RequiredArgsConstructor
public class DummyDevInit extends DummyObject {

    private final BCryptPasswordEncoder passwordEncoder;
    private final OpenApiServiceImpl openApiService;
    private final WaterPlaceRepository waterPlaceRepository;
    private final AccidentRepository accidentRepository;
    private final TripScheduleRepository tripScheduleRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.nickname}")
    private String adminNickname;

    @Profile("dev") // prod 모드에서는 실행되면 안된다.
    @Bean
    CommandLineRunner devInit(OpenApiServiceImpl openApiService, WaterPlaceRepository waterPlaceRepository,
                              AccidentRepository accidentRepository, TripScheduleRepository tripScheduleRepository,
                              MemberRepository memberRepository, ReviewRepository reviewRepository,
                              ReviewImageRepository reviewImageRepository) {
        return (args) -> {
            // 서버 실행시 무조건 실행된다.

            initializeAdminMember();

            fetchAndSaveData();

            /* 더미 데이터(주석 해제 후 서버 실행하고 반드시 다시 주석처리하기!!)  */
//            saveDummyData();
//            myPageDummyData();
        };
    }

    @Profile("prod")
    @Bean
    CommandLineRunner prodInit() {
        return (args) -> {
            // 서버 실행시 무조건 실행된다.

            initializeAdminMember();

            fetchAndSaveDataForProd();
            // saveDummyData();
        };
    }

    private void initializeAdminMember() {
        if (!memberRepository.existsByUsername(adminUsername)) {
            Member admin = Member.builder()
                    .email(adminEmail)
                    .memberName(adminNickname)
                    .nickname(adminNickname)
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(MemberEnum.ADMIN)
                    .build();
            memberRepository.save(admin);
        }
    }

    private void fetchAndSaveData() {
        openApiService.fetchAndSaveWaterPlacesData();
        openApiService.fetchAndSaveSpecialWeatherData();
        openApiService.fetchAndSaveNationalWeatherData();
    }

    private void fetchAndSaveDataForProd() {
       // openApiService.fetchAndSaveWaterPlacesData();
        openApiService.fetchAndSaveSpecialWeatherData();
        openApiService.fetchAndSaveNationalWeatherData();
    }

    private void saveDummyData() {
        // 경기도 계곡
        WaterPlace a = waterPlaceRepository.findById(20L).get();
        WaterPlace b = waterPlaceRepository.findById(21L).get();
        WaterPlace c = waterPlaceRepository.findById(22L).get();
        WaterPlace d = waterPlaceRepository.findById(26L).get();
        WaterPlace e = waterPlaceRepository.findById(28L).get();

        // 세종시 계곡
        WaterPlace f = waterPlaceRepository.findById(1122L).get();
        WaterPlace g = waterPlaceRepository.findById(1117L).get();
        WaterPlace h = waterPlaceRepository.findById(18L).get();
        WaterPlace i = waterPlaceRepository.findById(550L).get();
        WaterPlace j = waterPlaceRepository.findById(1119L).get();

        // 충북 계곡
        WaterPlace k = waterPlaceRepository.findById(476L).get();
        WaterPlace l = waterPlaceRepository.findById(1210L).get();
        WaterPlace m = waterPlaceRepository.findById(479L).get();
        WaterPlace n = waterPlaceRepository.findById(478L).get();
        WaterPlace o = waterPlaceRepository.findById(475L).get();

        // 강원도 계곡
        WaterPlace p = waterPlaceRepository.findById(112L).get();
        WaterPlace q = waterPlaceRepository.findById(114L).get();
        WaterPlace r = waterPlaceRepository.findById(125L).get();
        WaterPlace s = waterPlaceRepository.findById(145L).get();
        WaterPlace t = waterPlaceRepository.findById(147L).get();

        // 경북 계곡
        WaterPlace u = waterPlaceRepository.findById(763L).get();
        WaterPlace v = waterPlaceRepository.findById(723L).get();
        WaterPlace w = waterPlaceRepository.findById(731L).get();
        WaterPlace x = waterPlaceRepository.findById(781L).get();
        WaterPlace y = waterPlaceRepository.findById(839L).get();

        // 경기도 사고
        accidentRepository.save(Accident.builder()
                .waterPlace(a)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 15))
                .accidentCondition(AccidentEnum.INJURY)
                .peopleNum(5)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(a)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 2, 17))
                .accidentCondition(AccidentEnum.DISAPPEARANCE)
                .peopleNum(1)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(a)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 6, 19))
                .accidentCondition(AccidentEnum.DEATH)
                .peopleNum(3)
                .build());

        // 세종시 사고
        accidentRepository.save(Accident.builder()
                .waterPlace(f)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 1))
                .accidentCondition(AccidentEnum.INJURY)
                .peopleNum(1)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(f)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 17))
                .accidentCondition(AccidentEnum.DISAPPEARANCE)
                .peopleNum(6)
                .build());

        // 충북 사고
        accidentRepository.save(Accident.builder()
                .waterPlace(k)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 10, 1))
                .accidentCondition(AccidentEnum.INJURY)
                .peopleNum(21)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(k)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 5, 17))
                .accidentCondition(AccidentEnum.DISAPPEARANCE)
                .peopleNum(1)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(k)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 7, 30))
                .accidentCondition(AccidentEnum.DEATH)
                .peopleNum(5)
                .build());

        // 강원도 사고
        accidentRepository.save(Accident.builder()
                .waterPlace(p)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 17))
                .accidentCondition(AccidentEnum.INJURY)
                .peopleNum(40)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(p)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 7, 1))
                .accidentCondition(AccidentEnum.INJURY)
                .peopleNum(20)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(p)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 6, 22))
                .accidentCondition(AccidentEnum.INJURY)
                .peopleNum(16)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(p)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 29))
                .accidentCondition(AccidentEnum.DISAPPEARANCE)
                .peopleNum(4)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(p)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 11, 1))
                .accidentCondition(AccidentEnum.DEATH)
                .peopleNum(1)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(p)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 17))
                .accidentCondition(AccidentEnum.DEATH)
                .peopleNum(11)
                .build());

        // 경북 사고
        accidentRepository.save(Accident.builder()
                .waterPlace(u)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 17))
                .accidentCondition(AccidentEnum.INJURY)
                .peopleNum(2)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(u)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 17))
                .accidentCondition(AccidentEnum.DISAPPEARANCE)
                .peopleNum(30)
                .build());

        accidentRepository.save(Accident.builder()
                .waterPlace(u)
                .accidentDate(LocalDate.of(LocalDate.now().getYear(), 8, 27))
                .accidentCondition(AccidentEnum.DEATH)
                .peopleNum(8)
                .build());

        Member member1 = memberRepository.save(newMember("kakao_1234", "행복한여행가"));
        Member member2 = memberRepository.save(newMember("kakao_5678", "캠핑매니아"));
        Member member3 = memberRepository.save(newMember("kakao_9101", "아름다운자연"));
        Member member4 = memberRepository.save(newMember("kakao_1121", "가족여행러"));
        Member member5 = memberRepository.save(newMember("kakao_3141", "산림욕좋아요"));
        Member member6 = memberRepository.save(newMember("kakao_4231", "자연을사랑하는사람"));
        Member member7 = memberRepository.save(newMember("kakao_7777", "계곡짱짱맨"));

        List<TripSchedule> tripScheduleList = new ArrayList<>();
        TripSchedule tripSchedule1 = newTripSchedule(member1, a, LocalDate.of(2023, 8, 1), 5);
        TripSchedule tripSchedule2 = newTripSchedule(member1, b, LocalDate.of(2023, 8, 2), 2);
        TripSchedule tripSchedule3 = newTripSchedule(member1, c, LocalDate.of(2023, 8, 3), 8);
        TripSchedule tripSchedule4 = newTripSchedule(member1, d, LocalDate.of(2023, 8, 4), 10);
        TripSchedule tripSchedule5 = newTripSchedule(member1, e, LocalDate.of(2023, 8, 5), 4);
        TripSchedule tripSchedule6 = newTripSchedule(member1, f, LocalDate.of(2023, 8, 6), 3);
        TripSchedule tripSchedule7 = newTripSchedule(member1, g, LocalDate.of(2023, 8, 7), 5);
        TripSchedule tripSchedule8 = newTripSchedule(member1, h, LocalDate.of(2023, 8, 8), 5);
        TripSchedule tripSchedule9 = newTripSchedule(member1, i, LocalDate.of(2023, 8, 9), 6);
        TripSchedule tripSchedule10 = newTripSchedule(member1, j, LocalDate.of(2023, 8, 10), 7);
        TripSchedule tripSchedule11 = newTripSchedule(member1, k, LocalDate.of(2023, 8, 11), 8);
        TripSchedule tripSchedule12 = newTripSchedule(member1, l, LocalDate.of(2023, 8, 12), 11);
        TripSchedule tripSchedule13 = newTripSchedule(member1, m, LocalDate.of(2023, 8, 13), 2);
        TripSchedule tripSchedule14 = newTripSchedule(member1, n, LocalDate.of(2023, 8, 14), 4);
        TripSchedule tripSchedule15 = newTripSchedule(member1, o, LocalDate.of(2023, 8, 15), 3);
        TripSchedule tripSchedule16 = newTripSchedule(member1, p, LocalDate.of(2023, 8, 16), 5);
        TripSchedule tripSchedule17 = newTripSchedule(member1, q, LocalDate.of(2023, 8, 17), 6);
        TripSchedule tripSchedule18 = newTripSchedule(member1, r, LocalDate.of(2023, 8, 18), 8);
        TripSchedule tripSchedule19 = newTripSchedule(member1, s, LocalDate.of(2023, 8, 19), 9);
        TripSchedule tripSchedule20 = newTripSchedule(member1, t, LocalDate.of(2023, 8, 20), 1);
        TripSchedule tripSchedule21 = newTripSchedule(member1, u, LocalDate.of(2023, 8, 21), 4);
        TripSchedule tripSchedule22 = newTripSchedule(member1, v, LocalDate.of(2023, 8, 22), 6);
        TripSchedule tripSchedule23 = newTripSchedule(member1, w, LocalDate.of(2023, 8, 23), 5);
        TripSchedule tripSchedule24 = newTripSchedule(member1, x, LocalDate.of(2023, 8, 24), 5);
        TripSchedule tripSchedule25 = newTripSchedule(member1, y, LocalDate.of(2023, 8, 25), 5);

        TripSchedule tripSchedule26 = newTripSchedule(member2, a, LocalDate.of(2023, 6, 1), 11);
        TripSchedule tripSchedule27 = newTripSchedule(member2, b, LocalDate.of(2023, 7, 2), 5);
        TripSchedule tripSchedule28 = newTripSchedule(member2, c, LocalDate.of(2023, 7, 11), 3);
        TripSchedule tripSchedule29 = newTripSchedule(member2, d, LocalDate.of(2023, 7, 14), 2);
        TripSchedule tripSchedule30 = newTripSchedule(member2, e, LocalDate.of(2023, 7, 16), 7);
        TripSchedule tripSchedule31 = newTripSchedule(member2, f, LocalDate.of(2023, 7, 18), 15);
        TripSchedule tripSchedule32 = newTripSchedule(member2, g, LocalDate.of(2023, 7, 19), 25);
        TripSchedule tripSchedule33 = newTripSchedule(member2, h, LocalDate.of(2023, 7, 20), 8);
        TripSchedule tripSchedule34 = newTripSchedule(member2, i, LocalDate.of(2023, 7, 22), 11);
        TripSchedule tripSchedule35 = newTripSchedule(member2, j, LocalDate.of(2023, 7, 24), 9);
        TripSchedule tripSchedule36 = newTripSchedule(member2, k, LocalDate.of(2023, 7, 29), 3);
        TripSchedule tripSchedule37 = newTripSchedule(member2, l, LocalDate.of(2023, 8, 1), 6);
        TripSchedule tripSchedule38 = newTripSchedule(member2, m, LocalDate.of(2023, 8, 2), 8);
        TripSchedule tripSchedule39 = newTripSchedule(member2, n, LocalDate.of(2023, 8, 3), 11);
        TripSchedule tripSchedule40 = newTripSchedule(member2, o, LocalDate.of(2023, 8, 4), 25);
        TripSchedule tripSchedule41 = newTripSchedule(member2, p, LocalDate.of(2023, 8, 5), 10);
        TripSchedule tripSchedule42 = newTripSchedule(member2, q, LocalDate.of(2023, 8, 6), 7);
        TripSchedule tripSchedule43 = newTripSchedule(member2, r, LocalDate.of(2023, 8, 7), 4);
        TripSchedule tripSchedule44 = newTripSchedule(member2, s, LocalDate.of(2023, 8, 8), 6);
        TripSchedule tripSchedule45 = newTripSchedule(member2, t, LocalDate.of(2023, 8, 9), 5);
        TripSchedule tripSchedule46 = newTripSchedule(member2, u, LocalDate.of(2023, 8, 10), 5);
        TripSchedule tripSchedule47 = newTripSchedule(member2, v, LocalDate.of(2023, 8, 15), 1);
        TripSchedule tripSchedule48 = newTripSchedule(member2, w, LocalDate.of(2023, 8, 18), 4);
        TripSchedule tripSchedule49 = newTripSchedule(member2, x, LocalDate.of(2023, 8, 20), 3);
        TripSchedule tripSchedule50 = newTripSchedule(member2, y, LocalDate.of(2023, 8, 21), 5);

        TripSchedule tripSchedule51 = newTripSchedule(member3, a, LocalDate.of(2023, 6, 28), 4);
        TripSchedule tripSchedule52 = newTripSchedule(member3, b, LocalDate.of(2023, 6, 29), 5);
        TripSchedule tripSchedule53 = newTripSchedule(member3, c, LocalDate.of(2023, 6, 30), 15);
        TripSchedule tripSchedule54 = newTripSchedule(member3, d, LocalDate.of(2023, 7, 15), 5);
        TripSchedule tripSchedule55 = newTripSchedule(member3, e, LocalDate.of(2023, 7, 16), 7);
        TripSchedule tripSchedule56 = newTripSchedule(member3, f, LocalDate.of(2023, 7, 22), 3);
        TripSchedule tripSchedule57 = newTripSchedule(member3, g, LocalDate.of(2023, 7, 23), 3);
        TripSchedule tripSchedule58 = newTripSchedule(member3, h, LocalDate.of(2023, 7, 29), 6);
        TripSchedule tripSchedule59 = newTripSchedule(member3, i, LocalDate.of(2023, 7, 30), 4);
        TripSchedule tripSchedule60 = newTripSchedule(member3, j, LocalDate.of(2023, 8, 2), 5);
        TripSchedule tripSchedule61 = newTripSchedule(member3, k, LocalDate.of(2023, 8, 4), 4);
        TripSchedule tripSchedule62 = newTripSchedule(member3, l, LocalDate.of(2023, 8, 5), 29);
        TripSchedule tripSchedule63 = newTripSchedule(member3, m, LocalDate.of(2023, 8, 6), 3);
        TripSchedule tripSchedule64 = newTripSchedule(member3, n, LocalDate.of(2023, 8, 7), 4);
        TripSchedule tripSchedule65 = newTripSchedule(member3, o, LocalDate.of(2023, 8, 9), 6);
        TripSchedule tripSchedule66 = newTripSchedule(member3, p, LocalDate.of(2023, 8, 16), 7);
        TripSchedule tripSchedule67 = newTripSchedule(member3, q, LocalDate.of(2023, 8, 17), 9);
        TripSchedule tripSchedule68 = newTripSchedule(member3, r, LocalDate.of(2023, 8, 18), 2);
        TripSchedule tripSchedule69 = newTripSchedule(member3, s, LocalDate.of(2023, 8, 19), 3);
        TripSchedule tripSchedule70 = newTripSchedule(member3, t, LocalDate.of(2023, 8, 20), 5);
        TripSchedule tripSchedule71 = newTripSchedule(member3, u, LocalDate.of(2023, 8, 21), 6);
        TripSchedule tripSchedule72 = newTripSchedule(member3, v, LocalDate.of(2023, 8, 22), 4);
        TripSchedule tripSchedule73 = newTripSchedule(member3, w, LocalDate.of(2023, 8, 23), 2);
        TripSchedule tripSchedule74 = newTripSchedule(member3, x, LocalDate.of(2023, 8, 24), 4);
        TripSchedule tripSchedule75 = newTripSchedule(member3, y, LocalDate.of(2023, 8, 25), 5);

        TripSchedule tripSchedule76 = newTripSchedule(member4, a, LocalDate.of(2023, 6, 28), 3);
        TripSchedule tripSchedule77 = newTripSchedule(member4, b, LocalDate.of(2023, 7, 1), 4);
        TripSchedule tripSchedule78 = newTripSchedule(member4, c, LocalDate.of(2023, 7, 3), 5);
        TripSchedule tripSchedule79 = newTripSchedule(member4, d, LocalDate.of(2023, 7, 4), 6);
        TripSchedule tripSchedule80 = newTripSchedule(member4, e, LocalDate.of(2023, 7, 5), 7);
        TripSchedule tripSchedule81 = newTripSchedule(member4, f, LocalDate.of(2023, 7, 6), 8);
        TripSchedule tripSchedule82 = newTripSchedule(member4, g, LocalDate.of(2023, 7, 7), 9);
        TripSchedule tripSchedule83 = newTripSchedule(member4, h, LocalDate.of(2023, 7, 8), 10);
        TripSchedule tripSchedule84 = newTripSchedule(member4, i, LocalDate.of(2023, 8, 1), 11);
        TripSchedule tripSchedule85 = newTripSchedule(member4, j, LocalDate.of(2023, 8, 10), 12);
        TripSchedule tripSchedule86 = newTripSchedule(member4, k, LocalDate.of(2023, 8, 11), 13);
        TripSchedule tripSchedule87 = newTripSchedule(member4, l, LocalDate.of(2023, 8, 12), 14);
        TripSchedule tripSchedule88 = newTripSchedule(member4, m, LocalDate.of(2023, 8, 13), 15);
        TripSchedule tripSchedule89 = newTripSchedule(member4, n, LocalDate.of(2023, 8, 14), 16);
        TripSchedule tripSchedule90 = newTripSchedule(member4, o, LocalDate.of(2023, 8, 15), 17);
        TripSchedule tripSchedule91 = newTripSchedule(member4, p, LocalDate.of(2023, 8, 16), 5);
        TripSchedule tripSchedule92 = newTripSchedule(member4, q, LocalDate.of(2023, 8, 17), 18);
        TripSchedule tripSchedule93 = newTripSchedule(member4, r, LocalDate.of(2023, 8, 18), 19);
        TripSchedule tripSchedule94 = newTripSchedule(member4, s, LocalDate.of(2023, 8, 19), 2);
        TripSchedule tripSchedule95 = newTripSchedule(member4, t, LocalDate.of(2023, 8, 20), 7);
        TripSchedule tripSchedule96 = newTripSchedule(member4, u, LocalDate.of(2023, 8, 21), 7);
        TripSchedule tripSchedule97 = newTripSchedule(member4, v, LocalDate.of(2023, 8, 22), 9);
        TripSchedule tripSchedule98 = newTripSchedule(member4, w, LocalDate.of(2023, 8, 23), 10);
        TripSchedule tripSchedule99 = newTripSchedule(member4, x, LocalDate.of(2023, 8, 24), 11);
        TripSchedule tripSchedule100 = newTripSchedule(member4, y, LocalDate.of(2023, 8, 25), 12);

        TripSchedule tripSchedule101 = newTripSchedule(member5, a, LocalDate.of(2023, 6, 20), 3);
        TripSchedule tripSchedule102 = newTripSchedule(member5, b, LocalDate.of(2023, 6, 24), 6);
        TripSchedule tripSchedule103 = newTripSchedule(member5, c, LocalDate.of(2023, 6, 28), 5);
        TripSchedule tripSchedule104 = newTripSchedule(member5, d, LocalDate.of(2023, 6, 30), 5);
        TripSchedule tripSchedule105 = newTripSchedule(member5, e, LocalDate.of(2023, 7, 5), 1);
        TripSchedule tripSchedule106 = newTripSchedule(member5, f, LocalDate.of(2023, 7, 6), 2);
        TripSchedule tripSchedule107 = newTripSchedule(member5, g, LocalDate.of(2023, 7, 7), 15);
        TripSchedule tripSchedule108 = newTripSchedule(member5, h, LocalDate.of(2023, 8, 8), 19);
        TripSchedule tripSchedule109 = newTripSchedule(member5, i, LocalDate.of(2023, 8, 9), 5);
        TripSchedule tripSchedule110 = newTripSchedule(member5, j, LocalDate.of(2023, 8, 10), 5);
        TripSchedule tripSchedule111 = newTripSchedule(member5, k, LocalDate.of(2023, 8, 11), 5);
        TripSchedule tripSchedule112 = newTripSchedule(member5, l, LocalDate.of(2023, 8, 12), 5);
        TripSchedule tripSchedule113 = newTripSchedule(member5, m, LocalDate.of(2023, 8, 13), 5);
        TripSchedule tripSchedule114 = newTripSchedule(member5, n, LocalDate.of(2023, 8, 14), 5);
        TripSchedule tripSchedule115 = newTripSchedule(member5, o, LocalDate.of(2023, 8, 15), 5);
        TripSchedule tripSchedule116 = newTripSchedule(member5, p, LocalDate.of(2023, 8, 16), 5);
        TripSchedule tripSchedule117 = newTripSchedule(member5, q, LocalDate.of(2023, 8, 17), 5);
        TripSchedule tripSchedule118 = newTripSchedule(member5, r, LocalDate.of(2023, 8, 18), 5);
        TripSchedule tripSchedule119 = newTripSchedule(member5, s, LocalDate.of(2023, 8, 19), 5);
        TripSchedule tripSchedule120 = newTripSchedule(member5, t, LocalDate.of(2023, 8, 20), 5);
        TripSchedule tripSchedule121 = newTripSchedule(member5, u, LocalDate.of(2023, 8, 21), 5);
        TripSchedule tripSchedule122 = newTripSchedule(member5, v, LocalDate.of(2023, 8, 22), 5);
        TripSchedule tripSchedule123 = newTripSchedule(member5, w, LocalDate.of(2023, 8, 23), 5);
        TripSchedule tripSchedule124 = newTripSchedule(member5, x, LocalDate.of(2023, 8, 24), 5);
        TripSchedule tripSchedule125 = newTripSchedule(member5, y, LocalDate.of(2023, 8, 25), 5);

        TripSchedule tripSchedule126 = newTripSchedule(member6, a, LocalDate.of(2023, 8, 1), 10);
        TripSchedule tripSchedule127 = newTripSchedule(member6, b, LocalDate.of(2023, 8, 2), 12);
        TripSchedule tripSchedule128 = newTripSchedule(member6, c, LocalDate.of(2023, 8, 3), 13);
        TripSchedule tripSchedule129 = newTripSchedule(member6, d, LocalDate.of(2023, 8, 4), 14);
        TripSchedule tripSchedule130 = newTripSchedule(member6, e, LocalDate.of(2023, 8, 5), 15);
        TripSchedule tripSchedule131 = newTripSchedule(member6, f, LocalDate.of(2023, 8, 6), 16);
        TripSchedule tripSchedule132 = newTripSchedule(member6, g, LocalDate.of(2023, 8, 7), 17);
        TripSchedule tripSchedule133 = newTripSchedule(member6, h, LocalDate.of(2023, 8, 8), 18);
        TripSchedule tripSchedule134 = newTripSchedule(member6, i, LocalDate.of(2023, 8, 9), 19);
        TripSchedule tripSchedule135 = newTripSchedule(member6, j, LocalDate.of(2023, 8, 10), 39);
        TripSchedule tripSchedule136 = newTripSchedule(member6, k, LocalDate.of(2023, 8, 11), 38);
        TripSchedule tripSchedule137 = newTripSchedule(member6, l, LocalDate.of(2023, 8, 12), 37);
        TripSchedule tripSchedule138 = newTripSchedule(member6, m, LocalDate.of(2023, 8, 13), 36);
        TripSchedule tripSchedule139 = newTripSchedule(member6, n, LocalDate.of(2023, 8, 14), 35);
        TripSchedule tripSchedule140 = newTripSchedule(member6, o, LocalDate.of(2023, 8, 15), 34);
        TripSchedule tripSchedule141 = newTripSchedule(member6, p, LocalDate.of(2023, 8, 16), 33);
        TripSchedule tripSchedule142 = newTripSchedule(member6, q, LocalDate.of(2023, 8, 17), 32);
        TripSchedule tripSchedule143 = newTripSchedule(member6, r, LocalDate.of(2023, 8, 18), 31);
        TripSchedule tripSchedule144 = newTripSchedule(member6, s, LocalDate.of(2023, 8, 19), 30);
        TripSchedule tripSchedule145 = newTripSchedule(member6, t, LocalDate.of(2023, 8, 20), 29);
        TripSchedule tripSchedule146 = newTripSchedule(member6, u, LocalDate.of(2023, 8, 21), 28);
        TripSchedule tripSchedule147 = newTripSchedule(member6, v, LocalDate.of(2023, 8, 22), 27);
        TripSchedule tripSchedule148 = newTripSchedule(member6, w, LocalDate.of(2023, 8, 23), 26);
        TripSchedule tripSchedule149 = newTripSchedule(member6, x, LocalDate.of(2023, 8, 24), 25);
        TripSchedule tripSchedule150 = newTripSchedule(member6, y, LocalDate.of(2023, 8, 25), 24);

        TripSchedule tripSchedule151 = newTripSchedule(member7, a, LocalDate.of(2023, 8, 1), 1);
        TripSchedule tripSchedule152 = newTripSchedule(member7, b, LocalDate.of(2023, 8, 2), 2);
        TripSchedule tripSchedule153 = newTripSchedule(member7, c, LocalDate.of(2023, 8, 3), 3);
        TripSchedule tripSchedule154 = newTripSchedule(member7, d, LocalDate.of(2023, 8, 4), 4);
        TripSchedule tripSchedule155 = newTripSchedule(member7, e, LocalDate.of(2023, 8, 5), 5);
        TripSchedule tripSchedule156 = newTripSchedule(member7, f, LocalDate.of(2023, 8, 6), 6);
        TripSchedule tripSchedule157 = newTripSchedule(member7, g, LocalDate.of(2023, 8, 7), 7);
        TripSchedule tripSchedule158 = newTripSchedule(member7, h, LocalDate.of(2023, 8, 8), 8);
        TripSchedule tripSchedule159 = newTripSchedule(member7, i, LocalDate.of(2023, 8, 9), 9);
        TripSchedule tripSchedule160 = newTripSchedule(member7, j, LocalDate.of(2023, 8, 10), 40);
        TripSchedule tripSchedule161 = newTripSchedule(member7, k, LocalDate.of(2023, 8, 11), 41);
        TripSchedule tripSchedule162 = newTripSchedule(member7, l, LocalDate.of(2023, 8, 12), 42);
        TripSchedule tripSchedule163 = newTripSchedule(member7, m, LocalDate.of(2023, 8, 13), 43);
        TripSchedule tripSchedule164 = newTripSchedule(member7, n, LocalDate.of(2023, 8, 14), 44);
        TripSchedule tripSchedule165 = newTripSchedule(member7, o, LocalDate.of(2023, 8, 15), 45);
        TripSchedule tripSchedule166 = newTripSchedule(member7, p, LocalDate.of(2023, 8, 16), 46);
        TripSchedule tripSchedule167 = newTripSchedule(member7, q, LocalDate.of(2023, 8, 17), 47);
        TripSchedule tripSchedule168 = newTripSchedule(member7, r, LocalDate.of(2023, 8, 18), 48);
        TripSchedule tripSchedule169 = newTripSchedule(member7, s, LocalDate.of(2023, 8, 19), 49);
        TripSchedule tripSchedule170 = newTripSchedule(member7, t, LocalDate.of(2023, 8, 20), 50);
        TripSchedule tripSchedule171 = newTripSchedule(member7, u, LocalDate.of(2023, 8, 21), 51);
        TripSchedule tripSchedule172 = newTripSchedule(member7, v, LocalDate.of(2023, 8, 22), 52);
        TripSchedule tripSchedule173 = newTripSchedule(member7, w, LocalDate.of(2023, 8, 23), 53);
        TripSchedule tripSchedule174 = newTripSchedule(member7, x, LocalDate.of(2023, 8, 24), 54);
        TripSchedule tripSchedule175 = newTripSchedule(member7, y, LocalDate.of(2023, 8, 25), 55);

        tripScheduleList.add(tripSchedule1);
        tripScheduleList.add(tripSchedule2);
        tripScheduleList.add(tripSchedule3);
        tripScheduleList.add(tripSchedule4);
        tripScheduleList.add(tripSchedule5);
        tripScheduleList.add(tripSchedule6);
        tripScheduleList.add(tripSchedule7);
        tripScheduleList.add(tripSchedule8);
        tripScheduleList.add(tripSchedule9);
        tripScheduleList.add(tripSchedule10);
        tripScheduleList.add(tripSchedule11);
        tripScheduleList.add(tripSchedule12);
        tripScheduleList.add(tripSchedule13);
        tripScheduleList.add(tripSchedule14);
        tripScheduleList.add(tripSchedule15);
        tripScheduleList.add(tripSchedule16);
        tripScheduleList.add(tripSchedule17);
        tripScheduleList.add(tripSchedule18);
        tripScheduleList.add(tripSchedule19);
        tripScheduleList.add(tripSchedule20);
        tripScheduleList.add(tripSchedule21);
        tripScheduleList.add(tripSchedule22);
        tripScheduleList.add(tripSchedule23);
        tripScheduleList.add(tripSchedule24);
        tripScheduleList.add(tripSchedule25);
        tripScheduleList.add(tripSchedule26);
        tripScheduleList.add(tripSchedule27);
        tripScheduleList.add(tripSchedule28);
        tripScheduleList.add(tripSchedule29);
        tripScheduleList.add(tripSchedule30);
        tripScheduleList.add(tripSchedule31);
        tripScheduleList.add(tripSchedule32);
        tripScheduleList.add(tripSchedule33);
        tripScheduleList.add(tripSchedule34);
        tripScheduleList.add(tripSchedule35);
        tripScheduleList.add(tripSchedule36);
        tripScheduleList.add(tripSchedule37);
        tripScheduleList.add(tripSchedule38);
        tripScheduleList.add(tripSchedule39);
        tripScheduleList.add(tripSchedule40);
        tripScheduleList.add(tripSchedule41);
        tripScheduleList.add(tripSchedule42);
        tripScheduleList.add(tripSchedule43);
        tripScheduleList.add(tripSchedule44);
        tripScheduleList.add(tripSchedule45);
        tripScheduleList.add(tripSchedule46);
        tripScheduleList.add(tripSchedule47);
        tripScheduleList.add(tripSchedule48);
        tripScheduleList.add(tripSchedule49);
        tripScheduleList.add(tripSchedule50);
        tripScheduleList.add(tripSchedule51);
        tripScheduleList.add(tripSchedule52);
        tripScheduleList.add(tripSchedule53);
        tripScheduleList.add(tripSchedule54);
        tripScheduleList.add(tripSchedule55);
        tripScheduleList.add(tripSchedule56);
        tripScheduleList.add(tripSchedule57);
        tripScheduleList.add(tripSchedule58);
        tripScheduleList.add(tripSchedule59);
        tripScheduleList.add(tripSchedule60);
        tripScheduleList.add(tripSchedule61);
        tripScheduleList.add(tripSchedule62);
        tripScheduleList.add(tripSchedule63);
        tripScheduleList.add(tripSchedule64);
        tripScheduleList.add(tripSchedule65);
        tripScheduleList.add(tripSchedule66);
        tripScheduleList.add(tripSchedule67);
        tripScheduleList.add(tripSchedule68);
        tripScheduleList.add(tripSchedule69);
        tripScheduleList.add(tripSchedule70);
        tripScheduleList.add(tripSchedule71);
        tripScheduleList.add(tripSchedule72);
        tripScheduleList.add(tripSchedule73);
        tripScheduleList.add(tripSchedule74);
        tripScheduleList.add(tripSchedule75);
        tripScheduleList.add(tripSchedule76);
        tripScheduleList.add(tripSchedule77);
        tripScheduleList.add(tripSchedule78);
        tripScheduleList.add(tripSchedule79);
        tripScheduleList.add(tripSchedule80);
        tripScheduleList.add(tripSchedule81);
        tripScheduleList.add(tripSchedule82);
        tripScheduleList.add(tripSchedule83);
        tripScheduleList.add(tripSchedule84);
        tripScheduleList.add(tripSchedule85);
        tripScheduleList.add(tripSchedule86);
        tripScheduleList.add(tripSchedule87);
        tripScheduleList.add(tripSchedule88);
        tripScheduleList.add(tripSchedule89);
        tripScheduleList.add(tripSchedule90);
        tripScheduleList.add(tripSchedule91);
        tripScheduleList.add(tripSchedule92);
        tripScheduleList.add(tripSchedule93);
        tripScheduleList.add(tripSchedule94);
        tripScheduleList.add(tripSchedule95);
        tripScheduleList.add(tripSchedule96);
        tripScheduleList.add(tripSchedule97);
        tripScheduleList.add(tripSchedule98);
        tripScheduleList.add(tripSchedule99);
        tripScheduleList.add(tripSchedule100);
        tripScheduleList.add(tripSchedule101);
        tripScheduleList.add(tripSchedule102);
        tripScheduleList.add(tripSchedule103);
        tripScheduleList.add(tripSchedule104);
        tripScheduleList.add(tripSchedule105);
        tripScheduleList.add(tripSchedule106);
        tripScheduleList.add(tripSchedule107);
        tripScheduleList.add(tripSchedule108);
        tripScheduleList.add(tripSchedule109);
        tripScheduleList.add(tripSchedule110);
        tripScheduleList.add(tripSchedule111);
        tripScheduleList.add(tripSchedule112);
        tripScheduleList.add(tripSchedule113);
        tripScheduleList.add(tripSchedule114);
        tripScheduleList.add(tripSchedule115);
        tripScheduleList.add(tripSchedule116);
        tripScheduleList.add(tripSchedule117);
        tripScheduleList.add(tripSchedule118);
        tripScheduleList.add(tripSchedule119);
        tripScheduleList.add(tripSchedule120);
        tripScheduleList.add(tripSchedule121);
        tripScheduleList.add(tripSchedule122);
        tripScheduleList.add(tripSchedule123);
        tripScheduleList.add(tripSchedule124);
        tripScheduleList.add(tripSchedule125);
        tripScheduleList.add(tripSchedule126);
        tripScheduleList.add(tripSchedule127);
        tripScheduleList.add(tripSchedule128);
        tripScheduleList.add(tripSchedule129);
        tripScheduleList.add(tripSchedule130);
        tripScheduleList.add(tripSchedule131);
        tripScheduleList.add(tripSchedule132);
        tripScheduleList.add(tripSchedule133);
        tripScheduleList.add(tripSchedule134);
        tripScheduleList.add(tripSchedule135);
        tripScheduleList.add(tripSchedule136);
        tripScheduleList.add(tripSchedule137);
        tripScheduleList.add(tripSchedule138);
        tripScheduleList.add(tripSchedule139);
        tripScheduleList.add(tripSchedule140);
        tripScheduleList.add(tripSchedule141);
        tripScheduleList.add(tripSchedule142);
        tripScheduleList.add(tripSchedule143);
        tripScheduleList.add(tripSchedule144);
        tripScheduleList.add(tripSchedule145);
        tripScheduleList.add(tripSchedule146);
        tripScheduleList.add(tripSchedule147);
        tripScheduleList.add(tripSchedule148);
        tripScheduleList.add(tripSchedule149);
        tripScheduleList.add(tripSchedule150);
        tripScheduleList.add(tripSchedule151);
        tripScheduleList.add(tripSchedule152);
        tripScheduleList.add(tripSchedule153);
        tripScheduleList.add(tripSchedule154);
        tripScheduleList.add(tripSchedule155);
        tripScheduleList.add(tripSchedule156);
        tripScheduleList.add(tripSchedule157);
        tripScheduleList.add(tripSchedule158);
        tripScheduleList.add(tripSchedule159);
        tripScheduleList.add(tripSchedule160);
        tripScheduleList.add(tripSchedule161);
        tripScheduleList.add(tripSchedule162);
        tripScheduleList.add(tripSchedule163);
        tripScheduleList.add(tripSchedule164);
        tripScheduleList.add(tripSchedule165);
        tripScheduleList.add(tripSchedule166);
        tripScheduleList.add(tripSchedule167);
        tripScheduleList.add(tripSchedule168);
        tripScheduleList.add(tripSchedule169);
        tripScheduleList.add(tripSchedule170);
        tripScheduleList.add(tripSchedule171);
        tripScheduleList.add(tripSchedule172);
        tripScheduleList.add(tripSchedule173);
        tripScheduleList.add(tripSchedule174);
        tripScheduleList.add(tripSchedule175);
        tripScheduleRepository.saveAll(tripScheduleList);

        //경기도 리뷰
        reviewRepository.save(
                newReview(a, tripSchedule1, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 5, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(a, tripSchedule26, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 4,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(a, tripSchedule51, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 3,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(
                newReview(a, tripSchedule76, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.FINE,
                        waterPlaceRepository));
        reviewRepository.save(newReview(a, tripSchedule101, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 3,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(a, tripSchedule126, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 4,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(a, tripSchedule151, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 5,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));

        reviewRepository.save(
                newReview(b, tripSchedule2, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(b, tripSchedule27, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(b, tripSchedule52, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(b, tripSchedule77, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(b, tripSchedule102, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(b, tripSchedule127, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(b, tripSchedule152, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));

        reviewRepository.save(
                newReview(c, tripSchedule3, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(c, tripSchedule28, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(c, tripSchedule53, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(c, tripSchedule78, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(c, tripSchedule103, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(c, tripSchedule128, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(c, tripSchedule153, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(d, tripSchedule4, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(d, tripSchedule29, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(d, tripSchedule54, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(d, tripSchedule79, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
//        reviewRepository.save(newReview(d, tripSchedule104, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(d, tripSchedule129, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(d, tripSchedule154, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(e, tripSchedule5, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(e, tripSchedule30, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
//        reviewRepository.save(newReview(e, tripSchedule55, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(e, tripSchedule80, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(e, tripSchedule105, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(e, tripSchedule130, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(e, tripSchedule155, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        // 세종시 리뷰
        reviewRepository.save(
                newReview(f, tripSchedule6, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(f, tripSchedule31, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(f, tripSchedule56, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(f, tripSchedule81, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(f, tripSchedule106, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(f, tripSchedule131, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(f, tripSchedule156, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(g, tripSchedule7, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(g, tripSchedule32, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(g, tripSchedule57, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(g, tripSchedule82, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(g, tripSchedule107, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(g, tripSchedule132, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(g, tripSchedule157, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

//        reviewRepository.save(newReview(h, tripSchedule8, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(h, tripSchedule33, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(h, tripSchedule58, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(h, tripSchedule83, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(h, tripSchedule108, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(h, tripSchedule133, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(h, tripSchedule158, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(i, tripSchedule9, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(i, tripSchedule34, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(i, tripSchedule59, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(i, tripSchedule84, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(i, tripSchedule109, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(i, tripSchedule134, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(i, tripSchedule159, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(j, tripSchedule10, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(j, tripSchedule35, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
//        reviewRepository.save(newReview(j, tripSchedule60, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(j, tripSchedule85, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(j, tripSchedule110, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(j, tripSchedule135, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(j, tripSchedule160, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        // 충북 리뷰
        reviewRepository.save(
                newReview(k, tripSchedule11, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(k, tripSchedule36, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(k, tripSchedule61, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(k, tripSchedule86, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(k, tripSchedule111, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(k, tripSchedule136, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(k, tripSchedule161, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(l, tripSchedule12, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(l, tripSchedule37, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(l, tripSchedule62, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(l, tripSchedule87, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(l, tripSchedule112, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(l, tripSchedule137, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(l, tripSchedule162, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(m, tripSchedule13, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(m, tripSchedule38, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(m, tripSchedule63, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(m, tripSchedule88, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(m, tripSchedule113, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(m, tripSchedule138, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(m, tripSchedule163, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(n, tripSchedule14, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(n, tripSchedule39, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(n, tripSchedule64, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(n, tripSchedule89, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
//        reviewRepository.save(newReview(n, tripSchedule114, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(n, tripSchedule139, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(n, tripSchedule164, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

//        reviewRepository.save(newReview(o, tripSchedule15, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(o, tripSchedule40, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
//        reviewRepository.save(newReview(o, tripSchedule65, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(o, tripSchedule90, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(o, tripSchedule115, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(o, tripSchedule140, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(o, tripSchedule165, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        // 강원 리뷰
        reviewRepository.save(
                newReview(p, tripSchedule16, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(p, tripSchedule41, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(p, tripSchedule66, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(p, tripSchedule91, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(p, tripSchedule116, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(p, tripSchedule141, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(p, tripSchedule166, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(q, tripSchedule17, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
//        reviewRepository.save(newReview(q, tripSchedule42, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(q, tripSchedule67, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(q, tripSchedule92, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(q, tripSchedule117, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(q, tripSchedule142, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(q, tripSchedule167, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(r, tripSchedule18, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(r, tripSchedule43, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
//        reviewRepository.save(newReview(r, tripSchedule68, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(r, tripSchedule93, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(r, tripSchedule118, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(r, tripSchedule143, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(r, tripSchedule168, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(s, tripSchedule19, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(s, tripSchedule44, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(s, tripSchedule69, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(s, tripSchedule94, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
//        reviewRepository.save(newReview(s, tripSchedule119, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(s, tripSchedule144, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(s, tripSchedule169, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(t, tripSchedule20, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(t, tripSchedule45, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(t, tripSchedule70, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(t, tripSchedule95, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(t, tripSchedule120, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(t, tripSchedule145, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2, WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
//        reviewRepository.save(newReview(t, tripSchedule170, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));

        // 경북 리뷰
        reviewRepository.save(
                newReview(u, tripSchedule21, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(u, tripSchedule46, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(u, tripSchedule71, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(u, tripSchedule96, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(u, tripSchedule121, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(u, tripSchedule146, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(u, tripSchedule171, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(v, tripSchedule22, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(v, tripSchedule47, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(v, tripSchedule72, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(v, tripSchedule97, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(v, tripSchedule122, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(v, tripSchedule147, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(v, tripSchedule172, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(w, tripSchedule23, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(w, tripSchedule48, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
        reviewRepository.save(newReview(w, tripSchedule73, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4,
                WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(
                newReview(w, tripSchedule98, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY,
                        waterPlaceRepository));
        reviewRepository.save(newReview(w, tripSchedule123, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(w, tripSchedule148, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(w, tripSchedule173, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(x, tripSchedule24, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
        reviewRepository.save(newReview(x, tripSchedule49, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
//        reviewRepository.save(newReview(x, tripSchedule74, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(x, tripSchedule99, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(x, tripSchedule124, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(x, tripSchedule149, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(x, tripSchedule174, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));

        reviewRepository.save(
                newReview(y, tripSchedule25, "가족들과 함께 방문했는데, 너무 좋았어요. 다음에 또 가고 싶네요.", 2, WaterQualityReviewEnum.CLEAN,
                        waterPlaceRepository));
//        reviewRepository.save(newReview(y, tripSchedule50, "캠핑장이랑 가까워서 좋았어요. 주말에 가면 사람이 많아서 조금 시끄러울 수 있어요.", 3, WaterQualityReviewEnum.FINE, waterPlaceRepository));
//        reviewRepository.save(newReview(y, tripSchedule75, "수질이 아주 깨끗해서 수영하기 좋았어요. 그런데 주차장이 좁아서 차를 주차하는데 어려움을 겪었어요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
//        reviewRepository.save(newReview(y, tripSchedule100, "가족들과 함께 피크닉 가기 좋아요. 특히 아이들이 좋아할 것 같아요.", 4, WaterQualityReviewEnum.DIRTY, waterPlaceRepository));
        reviewRepository.save(newReview(y, tripSchedule125, "숲이 많아 공기가 좋고, 풍경도 아름다워요. 다만, 주변에 편의시설이 부족해서 아쉬웠어요.", 1,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(y, tripSchedule150, "계곡물이 시원해서 여름에 방문하기 좋아요. 다만, 주말에는 사람이 너무 많아서 조금 복잡해요.", 2,
                WaterQualityReviewEnum.CLEAN, waterPlaceRepository));
        reviewRepository.save(newReview(y, tripSchedule175, "이곳은 자연 그대로의 아름다움이 담긴 곳이에요. 푸른 물고기와 맑은 물이 인상적이었습니다.", 3,
                WaterQualityReviewEnum.FINE, waterPlaceRepository));
    }

    private void myPageDummyData() {

        // 경기도 계곡
        WaterPlace a = waterPlaceRepository.findById(20L).get();
        WaterPlace b = waterPlaceRepository.findById(21L).get();
        WaterPlace c = waterPlaceRepository.findById(22L).get();
        WaterPlace d = waterPlaceRepository.findById(26L).get();
        WaterPlace e = waterPlaceRepository.findById(28L).get();

        // 세종시 계곡
        WaterPlace f = waterPlaceRepository.findById(1122L).get();
        WaterPlace g = waterPlaceRepository.findById(1117L).get();
        WaterPlace h = waterPlaceRepository.findById(18L).get();
        WaterPlace i = waterPlaceRepository.findById(550L).get();
        WaterPlace j = waterPlaceRepository.findById(1119L).get();

        // 충북 계곡
        WaterPlace k = waterPlaceRepository.findById(476L).get();
        WaterPlace l = waterPlaceRepository.findById(1210L).get();
        WaterPlace m = waterPlaceRepository.findById(479L).get();
        WaterPlace n = waterPlaceRepository.findById(478L).get();
        WaterPlace o = waterPlaceRepository.findById(475L).get();

        // 강원도 계곡
        WaterPlace p = waterPlaceRepository.findById(112L).get();
        WaterPlace q = waterPlaceRepository.findById(114L).get();
        WaterPlace r = waterPlaceRepository.findById(125L).get();
        WaterPlace s = waterPlaceRepository.findById(145L).get();
        WaterPlace t = waterPlaceRepository.findById(147L).get();

        // 경북 계곡
        WaterPlace u = waterPlaceRepository.findById(763L).get();
        WaterPlace v = waterPlaceRepository.findById(723L).get();
        WaterPlace w = waterPlaceRepository.findById(731L).get();
        WaterPlace x = waterPlaceRepository.findById(781L).get();
        WaterPlace y = waterPlaceRepository.findById(839L).get();

        Member me = memberRepository.findByUsername("kakao_3094640543").get();

        LocalDate now = LocalDate.now();
        List<TripSchedule> tripScheduleList = new ArrayList<>();
        TripSchedule tripSchedule1 = newTripSchedule(me, a, LocalDate.of(2023, 8, 1), 5);
        TripSchedule tripSchedule2 = newTripSchedule(me, b, LocalDate.of(2023, 8, 2), 2);
        TripSchedule tripSchedule3 = newTripSchedule(me, c, LocalDate.of(2023, 8, 3), 8);
        TripSchedule tripSchedule4 = newTripSchedule(me, d, LocalDate.of(2023, 8, 4), 10);
        TripSchedule tripSchedule5 = newTripSchedule(me, e, LocalDate.of(2023, 8, 5), 4);
        TripSchedule tripSchedule6 = newTripSchedule(me, f, LocalDate.of(2023, 8, 6), 3);
        TripSchedule tripSchedule7 = newTripSchedule(me, g, LocalDate.of(2023, 8, 7), 5);
        TripSchedule tripSchedule8 = newTripSchedule(me, h, LocalDate.of(2023, 8, 8), 5);
        TripSchedule tripSchedule9 = newTripSchedule(me, i, LocalDate.of(2023, 8, 9), 6);
        TripSchedule tripSchedule10 = newTripSchedule(me, j, now.plusDays(3), 7);
        TripSchedule tripSchedule11 = newTripSchedule(me, k, now.plusDays(5), 8);
        TripSchedule tripSchedule12 = newTripSchedule(me, l, now.plusDays(7), 11);

        tripScheduleList.add(tripSchedule1);
        tripScheduleList.add(tripSchedule2);
        tripScheduleList.add(tripSchedule3);
        tripScheduleList.add(tripSchedule4);
        tripScheduleList.add(tripSchedule5);
        tripScheduleList.add(tripSchedule6);
        tripScheduleList.add(tripSchedule7);
        tripScheduleList.add(tripSchedule8);
        tripScheduleList.add(tripSchedule9);
        tripScheduleList.add(tripSchedule10);
        tripScheduleList.add(tripSchedule11);
        tripScheduleList.add(tripSchedule12);

        tripScheduleRepository.saveAll(tripScheduleList);
    }
}
