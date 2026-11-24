import org.exam.service.MathQuestionService;
import org.exam.service.exception.AlreadyExist;
import org.exam.service.exception.NotFound;
import org.exam.service.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MathQuestionServiceTests {

    private MathQuestionService service;

    @BeforeEach
    void setUp() {
        service = new MathQuestionService();
        // Очищаем хранилище перед каждым тестом, если в конструкторе есть стартовые вопросы
        for (Question q : service.getAll()) {
            service.remove(q);
        }
    }

    @Test
    void addByStrings_addsNewQuestionAndReturnsIt() {
        Question added = service.add("2 + 2", "4");

        assertThat(added).isEqualTo(new Question("2 + 2", "4"));
        assertThat(service.getAll()).containsExactly(added);
    }

    @Test
    void addByObject_addsNewQuestionAndReturnsIt() {
        Question question = new Question("3 * 3", "9");

        Question added = service.add(question);

        assertThat(added).isEqualTo(question);
        assertThat(service.getAll()).containsExactly(question);
    }

    @Test
    void add_throwsAlreadyExistWhenQuestionDuplicated() {
        service.add("2 + 2", "4");

        assertThatThrownBy(() -> service.add("2 + 2", "4"))
                .isInstanceOf(AlreadyExist.class);

        assertThatThrownBy(() -> service.add(new Question("2 + 2", "4")))
                .isInstanceOf(AlreadyExist.class);
    }

    @Test
    void remove_deletesExistingQuestionAndReturnsIt() {
        Question q1 = service.add("2 + 2", "4");
        Question q2 = service.add("5 - 1", "4");

        Question removed = service.remove(q1);

        assertThat(removed).isEqualTo(q1);
        assertThat(service.getAll()).containsExactly(q2);
    }

    @Test
    void remove_throwsNotFoundWhenStorageIsEmpty() {
        Question nonExistent = new Question("1 + 1", "2");

        assertThatThrownBy(() -> service.remove(nonExistent))
                .isInstanceOf(NotFound.class);
    }

    @Test
    void remove_throwsNotFoundWhenQuestionAbsent() {
        service.add("2 + 2", "4");
        Question absent = new Question("10 + 10", "20");

        assertThatThrownBy(() -> service.remove(absent))
                .isInstanceOf(NotFound.class);
    }

    @Test
    void getAll_returnsAllStoredQuestions() {
        Question q1 = service.add("2 + 2", "4");
        Question q2 = service.add("3 + 3", "6");

        Collection<Question> all = service.getAll();

        assertThat(all).hasSize(2).containsExactlyInAnyOrder(q1, q2);
    }

    @Test
    void getRandomQuestion_returnsExistingQuestion() {
        Question q1 = service.add("2 + 2", "4");
        Question q2 = service.add("3 + 3", "6");

        Question random = service.getRandomQuestion();

        assertThat(random).isIn(q1, q2);
    }

    @Test
    void getRandomQuestion_throwsNotFoundWhenStorageIsEmpty() {
        assertThatThrownBy(() -> service.getRandomQuestion())
                .isInstanceOf(NotFound.class);
    }
}
