package com.example.sweater.Controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {
    private final MessageRepository messageRepository;
    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            Map<String, Object> model
    ) throws IOException {
        Message message = new Message(text, tag, user);
        if (file != null && !file.getOriginalFilename().isEmpty()){
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()){
                uploadDirectory.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);
        }
        messageRepository.save(message);
        Iterable<Message> all = messageRepository.findAll();
        model.put("messages", all);
        return "main";
    }
}