package org.exam.service;

import org.exam.service.exception.AlreadyExist;
import org.exam.service.exception.NotFound;
import org.exam.service.model.Question;
import org.exam.service.model.QuestionService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JavaQuestionService implements QuestionService {

    private final Set<Question> questions = new HashSet<>();
    private final Random random = new Random();

    @Override
    public Question add(String question, String answer) {
        return add(new Question(question, answer));
    }

    @Override
    public Question add(Question question) {
        if (!questions.add(question)) {
            throw new AlreadyExist("Такой вопрос уже существует");
        }
        return question;
    }

    @Override
    public Question remove(Question question) {
        if (!questions.remove(question)) {
            throw new NotFound("Вопрос не найден");
        }
        return question;
    }

    @Override
    public Collection<Question> getAll() {
        return Collections.unmodifiableSet(questions);
    }

    @Override
    public Question getRandomQuestion() {
        if (questions.isEmpty()) {
            throw new NotFound("Хранилище вопросов пусто");
        }
        int targetIndex = random.nextInt(questions.size());
        int currentIndex = 0;
        for (Question question : questions) {
            if (currentIndex++ == targetIndex) {
                return question;
            }
        }
        return null;
    }
}
