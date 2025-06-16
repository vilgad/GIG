package com.snippet.gig.pojo;

import java.util.List;

/*@Getter
@Setter
public class TelegramResponse {
    private List<Result> result;
}

@Getter
@Setter
class Result {
    private Message message;
}

@Getter
@Setter
class Message {
    private Chat chat;
}

@Getter
@Setter
class Chat {
    private Long id;
    private String first_name;
    private String username;
}*/

public record TelegramResponse(List<Result> result) {
}





