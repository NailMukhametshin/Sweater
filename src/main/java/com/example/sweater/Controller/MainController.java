package com.example.sweater.Controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repository.MessageRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    private final MessageRepository messageRepository;

    public MainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String greeting(
    ) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            Model model,
            @RequestParam(required = false, defaultValue = "") String query
    ) {
        Iterable<Message> all;
        if (!query.isEmpty()) {
            all = messageRepository.findAllByTag(query);
        } else {
            all = messageRepository.findAll();
        }
        model.addAttribute("messages", all);
        model.addAttribute("query", query);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model
    ) {
        Message message = new Message(text, tag, user);
        messageRepository.save(message);
        Iterable<Message> all = messageRepository.findAll();
        model.put("messages", all);
        return "main";
    }
}