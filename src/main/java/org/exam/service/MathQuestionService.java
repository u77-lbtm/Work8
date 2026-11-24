package org.exam.service;

import org.exam.service.exception.AlreadyExist;
import org.exam.service.exception.NotFound;
import org.exam.service.model.Question;
import org.exam.service.model.QuestionService;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class MathQuestionService implements org.exam.service.model.QuestionService {

    private final Set<Question> questions = new HashSet<>();
    private final Random random = new Random();


    public MathQuestionService() {
        add("2 + 2", "4");
        add("5 * 5", "25");
        add("10 - 3", "7");
        add("12 / 4", "3");
        add("9 + 6", "15");
    }

    @Override
    public Question add(String question, String answer) {
        Question q = new Question(question, answer);
        // add возвращает false, если такой объект уже есть в Set
        if (!questions.add(q)) {
            throw new AlreadyExist("Вопрос уже существует");

        }
        return q;
    }

    public Question add(Question question) {
        if (!questions.add(question)) {
            throw new AlreadyExist("Вопрос уже существует");
        }
        return question;
    }


    @Override
    public Question remove(Question question) {
        // Set.remove вернёт false, если объекта нет
        if (!questions.remove(question)) {
            throw new NotFound("Вопрос не найден");
        }
        return question;
    }

    @Override
    public Collection<Question> getAll() {
        // возвращаем копию, чтобы снаружи не могли менять наше хранилище
        return Set.copyOf(questions);
    }

    @Override
    public Question getRandomQuestion() {
        if (questions.isEmpty()) {
            throw new NotFound("Список вопросов пуст");
        }

        // Set не поддерживает индексы, поэтому идём через списочный обход
        int index = random.nextInt(questions.size());
        int i = 0;
        for (Question q : questions) {
            if (i++ == index) {
                return q;
            }
        }
        throw new NotFound("Хранилище пусто");
    }
}
