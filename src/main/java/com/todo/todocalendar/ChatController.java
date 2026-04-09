package com.todo.todocalendar;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // ✅ USER → ADMIN
    @MessageMapping("/sendToAdmin")
public void sendToAdmin(@Payload ChatMessage message, Principal principal) {

    String sender = principal != null ? principal.getName().trim().toLowerCase() : "unknown";
    message.setSender(sender);
    message.setReceiver("admin");

    System.out.println("USER → ADMIN: " + sender);

    messagingTemplate.convertAndSendToUser(
            "admin",
            "/queue/messages",
            message
    );
}

@MessageMapping("/sendToUser")
public void sendToUser(@Payload ChatMessage message, Principal principal) {

    String sender = principal != null ? principal.getName() : "admin";
    message.setSender(sender);

    String receiver = message.getReceiver().trim().toLowerCase(); // 🔥 FIX
    message.setReceiver(receiver);

    System.out.println("ADMIN → USER: " + receiver);

    messagingTemplate.convertAndSendToUser(
            receiver,
            "/queue/messages",
            message
    );
}
}