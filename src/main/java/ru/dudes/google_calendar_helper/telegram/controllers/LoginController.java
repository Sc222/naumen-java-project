package ru.dudes.google_calendar_helper.telegram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.dudes.google_calendar_helper.db.repositories.UserRepository;
import ru.dudes.google_calendar_helper.telegram.controllers.core.BotController;
import ru.dudes.google_calendar_helper.telegram.controllers.core.BotRequestMapping;

@BotController
public class LoginController {

    @Value("${loginURL}")
    private String homepage;

    private final UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BotRequestMapping(value = "/login")
    public SendMessage processLoginCommand(Update update) {
        var message = update.getMessage();
        var response = new SendMessage();
        response.setChatId(String.valueOf(message.getChatId()));

        var user = userRepository.findByChatId(String.valueOf(message.getChatId()));

        String responseText;
        if (user != null)  //todo move to helper method
            responseText = "You are already logged in.\nPlease type /logout and than /login again if you want to login with different account.";
        else {
            var loginUrl = String.format("Login url:\n%s?chatId=%d", homepage, update.getMessage().getChatId());
            //!!!localhost links don't highlight
            //todo: format text as Please follow the link below to login
            responseText = loginUrl;
        }
        response.setText(responseText);

        return response;
    }
}
