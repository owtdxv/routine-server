package com.codruwh.routine.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentRequest;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    @Value("${google.gemini.project.id}")
    private String projectId;

    @Value("${google.gemini.location}")
    private String location;

    String modelName = "gemini-2.5-flash-lite";


    /**
     * 메인화면에 표시되는 Sera의 건강 팁을 반환합니다
     * @return 응답(문자열))
     */
    public String getSerasTip() {
        String prompt = "한국어로 건강에 도움이 되는 간단한 팁을 1~2문장으로만 제공해 주세요. 이모지, 해시태그, 불필요한 설명은 제외해 주세요.";

        System.out.println(projectId);
        System.out.println(location);
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            GenerateContentResponse response = model.generateContent(prompt);
            String text = ResponseHandler.getText(response);
            if(text != null && !text.isBlank())
                text = text.trim();
            
            return (text == null || text.isBlank()) ? "비타민 D는 강력한 면역력 조절 기능도 가지고 있어요. 햇빛 노출이 어렵다면 식품이나 보충제를 통해 섭취하는 것도 방법이에요." : text;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return "비타민 D는 강력한 면역력 조절 기능도 가지고 있어요. 햇빛 노출이 어렵다면 식품이나 보충제를 통해 섭취하는 것도 방법이에요.";
        }
    }
}
