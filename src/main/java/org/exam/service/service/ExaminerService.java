package org.exam.service.service;


import org.exam.service.model.Question;
import java.util.Collection;


public interface ExaminerService {
    Collection<Question> getQuestions(int amount);
}
