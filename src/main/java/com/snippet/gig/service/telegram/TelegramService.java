package com.snippet.gig.service.telegram;

import com.snippet.gig.pojo.TelegramResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService implements ITelegramService {
    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendMessage(Long chatId, String message) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + message;
        restTemplate.getForObject(url, String.class);
    }

    @Override
    public TelegramResponse getTelegramResponse() {
        String url = "https://api.telegram.org/bot"+botToken+"/getUpdates";
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, TelegramResponse.class);
    }
}
