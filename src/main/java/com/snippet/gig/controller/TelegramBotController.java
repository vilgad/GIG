package com.snippet.gig.controller;

import com.snippet.gig.pojo.TelegramResponse;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.telegram.ITelegramService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/telegram-bot")
@Tag(name = "Telegram Bot APIs")
public class TelegramBotController {
    private final ITelegramService telegramService;

    public TelegramBotController(ITelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private/fetch-user-updates")
    public ResponseEntity<ApiResponse> getBotUpdates() {
        TelegramResponse response = telegramService.getTelegramResponse();
        return ResponseEntity.ok(
                new ApiResponse(
                        "Telegram response fetched Successfully",
                        response
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/private/send-message")
    public ResponseEntity<ApiResponse> getApiResponse(
            @RequestParam String message,
            @RequestParam Long chatId
    ) {
        telegramService.sendMessage(chatId, message);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Telegram response fetched Successfully",
                        null
                ));
    }
}
