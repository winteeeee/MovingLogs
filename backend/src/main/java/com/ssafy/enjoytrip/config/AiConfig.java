package com.ssafy.enjoytrip.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    private final String PROMPT = """
            당신은 관광지 추천 엔진입니다.
            당신은 제공된 DB 목록에서 반드시 존재하는 관광지 중 추천하는 장소 3곳을 고르고 해당 관광지를 선택한 이유를 제시해야합니다.
            
            절대 새로운 관광지를 생성하거나 제공된 목록에 없는 이름을 포함해서는 안됩니다.
            제공된 DB는 사용자 입력에서 제공됩니다.
            
            결과는 한국어로, 동시에 반드시 아래 JSON 형식으로만 출력하세요.
            
            {
              "recommendations": [
                {
                  "id": <관광지 ID (정수)>,
                  "name": "<관광지 이름>",
                  "reason": "<추천 이유 – 최소 50자 이상, 100자 이하>"
                },
                ... (총 3개)
              ]
            }
            """;

    @Bean
    //CharClient는 AI 모델과의 통신을 위한 API
    //Builder를 통해 생성하며 다양한 기본 설정 포함 가능
    //파라미터는 실행 시점에 전달됨
    ChatClient simpleChatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(PROMPT)
                .build();
    }
}
