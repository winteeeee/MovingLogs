package com.ssafy.enjoytrip.features.ai.adapter.out.chatgpt;

import com.ssafy.enjoytrip.features.ai.domain.AttractionRecommendation;
import com.ssafy.enjoytrip.features.ai.application.port.out.RecommendAttractionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatGptAdapter implements RecommendAttractionPort {
    private final ChatClient simpleChatClient;

    @Override
    public AttractionRecommendation attractionRecommend(String userInput) {
        return simpleChatClient.prompt()
                .user(userInput)
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }
}
