package ru.dudes.google_calendar_helper.telegram.controllers.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BotApiMethodController {

    private static final Logger logger = LoggerFactory.getLogger(BotApiMethodContainer.class);

    private final Object bean;
    private final Method method;
    private final Process processUpdate;

    public BotApiMethodController(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
        processUpdate = typeListReturnDetect() ? this::processList : this::processSingle;
    }

    public abstract boolean successUpdatePredicate(Update update);

    public List<PartialBotApiMethod> process(Update update) {
        if (!successUpdatePredicate(update)) return null;
        try {
            return processUpdate.accept(update);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("bad invoke method", e);
        }
        return null;
    }

    boolean typeListReturnDetect() {
        return List.class.equals(method.getReturnType());
    }

    private List<PartialBotApiMethod> processSingle(Update update) throws InvocationTargetException, IllegalAccessException {
        var botApiMethod = (PartialBotApiMethod) method.invoke(bean, update);
        return botApiMethod != null ? Collections.singletonList(botApiMethod) : new ArrayList<>(0);
    }

    private List<PartialBotApiMethod> processList(Update update) throws InvocationTargetException, IllegalAccessException {
        var botApiMethods = (List<PartialBotApiMethod>) method.invoke(bean, update);
        return botApiMethods != null ? botApiMethods : new ArrayList<>(0);
    }

    private interface Process {
        List<PartialBotApiMethod> accept(Update update) throws InvocationTargetException, IllegalAccessException;
    }
}