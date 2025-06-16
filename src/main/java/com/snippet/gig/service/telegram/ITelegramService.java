package com.snippet.gig.service.telegram;

public interface ITelegramService {
    void sendMessage(Long chatId, String message);
}
