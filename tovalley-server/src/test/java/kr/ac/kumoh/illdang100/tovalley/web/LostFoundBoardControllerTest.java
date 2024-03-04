package kr.ac.kumoh.illdang100.tovalley.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.kumoh.illdang100.tovalley.domain.comment.CommentRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundBoardRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.lost_found_board.LostFoundEnum;
import kr.ac.kumoh.illdang100.tovalley.domain.member.Member;
import kr.ac.kumoh.illdang100.tovalley.domain.member.MemberRepository;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlace;
import kr.ac.kumoh.illdang100.tovalley.domain.water_place.WaterPlaceRepository;
import kr.ac.kumoh.illdang100.tovalley.dummy.DummyObject;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtProcess;
import kr.ac.kumoh.illdang100.tovalley.security.jwt.JwtVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;

import static kr.ac.kumoh.illdang100.tovalley.dto.lost_found_board.LostFoundBoardReqDto.LostFoundBoardSaveReqDto;
import static kr.ac.kumoh.illdang100.tovalley.util.TokenUtil.createTestAccessToken;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Sql("classpath:db/teardown.sql")
class LostFoundBoardControllerTest extends DummyObject {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LostFoundBoardRepository lostFoundBoardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private WaterPlaceRepository waterPlaceRepository;

    @Autowired
    private JwtProcess jwtProcess;
    
    private String accessToken;

    @BeforeEach
    public void setUp() {
        dataSetting();
        accessToken = createTestAccessToken(memberRepository, jwtProcess, "kakao_123");
    }

    @Test
    @DisplayName(value = "분실물 게시글 등록")
    void saveLostFoundBoard() throws Exception {
        // given
        Long waterPlaceId = 1L;
        LostFoundBoardSaveReqDto lostFoundBoardSaveReqDto = new LostFoundBoardSaveReqDto("LOST", waterPlaceId, "잃어버림", "지갑 잃어버렸어요", null);

        MockMultipartFile file1 = new MockMultipartFile("postImage", "image1.jpg", "image/jpeg", "some image".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("postImage", "image2.jpg", "image/jpeg", "some image".getBytes());

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders.multipart("/api/auth/lostItem")
                        .file(file1)
                        .file(file2)
                        .param("category", lostFoundBoardSaveReqDto.getCategory())
                        .param("waterPlaceId", lostFoundBoardSaveReqDto.getWaterPlaceId().toString())
                        .param("title", lostFoundBoardSaveReqDto.getTitle())
                        .param("content", lostFoundBoardSaveReqDto.getContent())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .cookie(new Cookie(JwtVO.ACCESS_TOKEN, accessToken))
        );

        // then
        resultActions
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    private void dataSetting() {
        Member member = newMember("kakao_123", "일당백");
        memberRepository.save(member);

        WaterPlace waterPlace1 = newWaterPlace(1L, "금오계곡", "경상북도", 3.0, 3);
        waterPlaceRepository.save(waterPlace1);
        WaterPlace waterPlace2 = newWaterPlace(2L, "대구계곡", "경상북도", 3.0, 3);
        waterPlaceRepository.save(waterPlace2);

        lostFoundBoardRepository.save(newMockLostFoundBoard(1L, "지갑 보신 분", "지갑", member, false, LostFoundEnum.LOST, waterPlace1));
        lostFoundBoardRepository.save(newMockLostFoundBoard(2L, "지갑 찾습니다.", "지갑", member, false, LostFoundEnum.LOST, waterPlace1));
        lostFoundBoardRepository.save(newMockLostFoundBoard(3L, "폰 찾았어요", "아이폰14", member, false, LostFoundEnum.FOUND, waterPlace2));
        lostFoundBoardRepository.save(newMockLostFoundBoard(4L, "폰 잃어버리신 분", "갤럭시S20", member, true, LostFoundEnum.FOUND, waterPlace2));

        commentRepository.save(newMockComment(1L, 1L, member));
        commentRepository.save(newMockComment(2L, 1L, member));

        em.clear();
    }
}