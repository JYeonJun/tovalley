package kr.ac.kumoh.illdang100.tovalley.domain.chat;

import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chat_message")
public class ChatMessage {

    @Id
    private String id;
    @Indexed
    private Long chatRoomId;
    private Long senderId;
    private String content;

    // ZonedDateTime 값을 String으로 변환하여 저장
    // ZonedDateTime을 사용하면 특정 시간대에서의 시간을 정확히 표현할 수 있다.
    // 예를 들어, 사용자가 다른 시간대에서 메시지를 보냈다면, ZonedDateTime을 사용해 메시지를 보낸 시간을 해당 시간대의 시간으로 정확하게 표시할 수 있다.
    private LocalDateTime createdAt;
    private int readCount;

    private ChatType chatType; // 채팅 타입 필드 추가('TEXT', 'IMAGE')

    private String imageName;
    private String imageUrl;
}