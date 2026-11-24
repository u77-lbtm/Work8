import org.exam.service.MathQuestionService;
import org.exam.service.exception.NotEnoughQuestion;
import org.exam.service.model.Question;
import org.exam.service.service.ExaminerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTests {

    @Mock
    private MathQuestionService questionService;

    private ExaminerServiceImpl examiner;

    @BeforeEach
    void setUp() {
        examiner = new ExaminerServiceImpl(questionService);
    }

    @Test
    void getQuestions_returnsRequestedAmountWithoutDuplicates() {
        List<Question> pool = List.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2"),
                new Question("Q3", "A3"),
                new Question("Q4", "A4")
        );
        when(questionService.getAll()).thenReturn(pool);

        Collection<Question> result = examiner.getQuestions(3);

        assertThat(result)
                .hasSize(3)
                .doesNotHaveDuplicates()
                .isSubsetOf(pool);
    }

    @Test
    void getQuestions_returnsAllQuestionsWhenAmountEqualsAvailable() {
        List<Question> pool = List.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2")
        );
        when(questionService.getAll()).thenReturn(pool);

        Collection<Question> result = examiner.getQuestions(2);

        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(pool);
    }

    @Test
    void getQuestions_throwsNotEnoughQuestionWhenAmountIsZero() {
        when(questionService.getAll()).thenReturn(List.of(
                new Question("Q1", "A1")
        ));

        assertThatThrownBy(() -> examiner.getQuestions(0))
                .isInstanceOf(NotEnoughQuestion.class);
    }

    @Test
    void getQuestions_throwsNotEnoughQuestionWhenAmountExceedsAvailable() {
        when(questionService.getAll()).thenReturn(List.of(
                new Question("Q1", "A1"),
                new Question("Q2", "A2")
        ));

        assertThatThrownBy(() -> examiner.getQuestions(3))
                .isInstanceOf(NotEnoughQuestion.class);
    }

    @Test
    void getQuestions_throwsNotEnoughQuestionWhenStorageIsEmpty() {
        when(questionService.getAll()).thenReturn(List.of());

        assertThatThrownBy(() -> examiner.getQuestions(1))
                .isInstanceOf(NotEnoughQuestion.class);
    }
}
