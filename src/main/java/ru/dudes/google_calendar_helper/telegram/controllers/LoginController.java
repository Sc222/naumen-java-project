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

    private final UserRepository userRepository;
    @Value("${loginURL}")
    private String homepage;

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
        if (user != null) {
            response.setText("You are already logged in.\nPlease type /logout and than /login again if you want to login with different account.");
            return response;
        } else {
            var url = homepage + "?chatId=" + message.getChatId();
            response.setText("Your login url:\n" + url);
            /* TODO normal buttons + buttonControllers on production build
            if (homepage.startsWith("http://localhost")){ //localhost links are not valid links for telegram
                response.setText("Your login url:\n"+url);
            }
            else {
                response.setText("Tap the button to open browser and login:");
                var button = ResponseHelper.createKeyboardButton("Login", "/callback-login", url);
                var keyboard = List.of(List.of(button));
                var replyMarkup = new InlineKeyboardMarkup();
                replyMarkup.setKeyboard(keyboard);
                response.setReplyMarkup(replyMarkup);
            }
            */
        }
        return response;
    }
}
