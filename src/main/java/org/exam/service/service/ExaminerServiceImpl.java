package org.exam.service.service;

import org.exam.service.exception.NotEnoughQuestion;
import org.exam.service.model.Question;
import org.exam.service.model.QuestionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class ExaminerServiceImpl implements ExaminerService {

    private final QuestionService questionService;

    public ExaminerServiceImpl(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public Collection<Question> getQuestions(int amount) {
        Collection<Question> allQuestions = questionService.getAll();

        if (amount <= 0 || amount > allQuestions.size()) {
            throw new NotEnoughQuestion("Недостаточно вопросов для генерации экзамена: запрошено " + amount);
        }

        Set<Question> result = new HashSet<>();
        while (result.size() < amount) {
            result.add(questionService.getRandomQuestion());
        }

        return result;
    }
}
