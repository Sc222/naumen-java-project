package ru.dudes.google_calendar_helper.telegram.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.dudes.google_calendar_helper.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseUtils {


    public static final String NOT_LOGGED_IN = "You are not logged in.\nPlease type /login to login or service will not work";

    public static SendMessage generateNotLoggedInResponse(String chatId) {
        var response = new SendMessage();
        response.setChatId(chatId);
        response.setText(NOT_LOGGED_IN);
        return response;
    }

    public static SendMessage createSendMessage(String chatId, String text) {
        var response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        return response;
    }

    public static SendMessage createSendMessage(String chatId, String text, InlineKeyboardMarkup replyMarkup) {
        var response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        response.setReplyMarkup(replyMarkup);
        return response;
    }

    public static EditMessageText createEditMessage(String chatId, int messageId, String text, InlineKeyboardMarkup replyMarkup) {
        var response = new EditMessageText();
        response.setChatId(chatId);
        response.setMessageId(messageId);
        response.setText(text);
        response.setReplyMarkup(replyMarkup);
        return response;
    }


    public static InlineKeyboardButton createKeyboardButton(String text, String callbackData) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public static InlineKeyboardButton createKeyboardButton(String text, String callbackData, String url) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        button.setUrl(url);
        return button;
    }

    public static List<InlineKeyboardButton> createCalendarsAndHelpRow() {
        var calendarButton = ResponseUtils.createKeyboardButton("Calendars", "/callback-calendars 0");
        var otherButton = ResponseUtils.createKeyboardButton("Help", "/callback-help");
        return List.of(calendarButton, otherButton);
    }

    public static List<InlineKeyboardButton> createPaginationButtonRow(String callbackUrl, int currentPage, int pagesCount) {
        var previousButton = ResponseUtils.createKeyboardButton("Previous", callbackUrl + " " + (currentPage - 1));
        var nextButton = ResponseUtils.createKeyboardButton("Next", callbackUrl + " " + (currentPage + 1));
        List<InlineKeyboardButton> row = new ArrayList<>();
        if (currentPage > 0)
            row.add(previousButton);
        if (currentPage < pagesCount - 1)
            row.add(nextButton);
        return row;
    }

    public static List<InlineKeyboardButton> createDatePaginationButtonRow(String callbackUrl, Date currentPage) {
        var previousDay = TimeUtils.DATE_FORMAT_SHORT.format(TimeUtils.getPreviousDay(currentPage));
        var nextDay = TimeUtils.DATE_FORMAT_SHORT.format(TimeUtils.getNextDay(currentPage));
        var previousButton = ResponseUtils.createKeyboardButton("Previous", callbackUrl + " " + previousDay);
        var nextButton = ResponseUtils.createKeyboardButton("Next", callbackUrl + " " + nextDay);
        return List.of(previousButton, nextButton);
    }

    public static List<InlineKeyboardButton> createEntriesButtonRow(List<ButtonEntry> entries) {
        return entries.stream()
                .map(entry -> createKeyboardButton(entry.getText(), entry.getCallbackData()))
                .collect(Collectors.toList());
    }

    public static InlineKeyboardMarkup createKeyboardMarkupFromRows(List<List<InlineKeyboardButton>> rows) {
        var replyMarkup = new InlineKeyboardMarkup();
        replyMarkup.setKeyboard(rows);
        return replyMarkup;
    }
}
