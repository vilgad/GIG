package com.snippet.gig.service.telegram;

import com.snippet.gig.pojo.TelegramResponse;

public interface ITelegramService {
    void sendMessage(Long chatId, String message);

    TelegramResponse getTelegramResponse();
}
