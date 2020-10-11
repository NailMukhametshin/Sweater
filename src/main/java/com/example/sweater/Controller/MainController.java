package com.example.sweater.Controller;

import com.example.sweater.domain.Message;
import com.example.sweater.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/")
    public String greeting(
    ) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            Map<String, Object> model
    ) {
        Iterable<Message> all = messageRepository.findAll();
        model.put("messages", all);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model
    ) {
        Message message = new Message(text, tag);
        messageRepository.save(message);
        Iterable<Message> all = messageRepository.findAll();
        model.put("messages", all);
        return "main";
    }

    @PostMapping("/filter")
    public String filter(
            @RequestParam String query,
            Map<String, Object> model
    ) {
        Iterable<Message> messages;
        if (!query.isEmpty()) {
            messages = messageRepository.findAllByTag(query);
        } else {
            messages = messageRepository.findAll();
        }
        model.put("messages", messages);
        return "main";
    }
}