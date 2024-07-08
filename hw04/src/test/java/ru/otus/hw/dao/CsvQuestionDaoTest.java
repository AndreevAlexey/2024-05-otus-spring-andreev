package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@TestPropertySource("classpath:test-application.yml")
@SpringBootTest(classes = {CsvQuestionDao.class})
public class CsvQuestionDaoTest {

    private static final int rightAnswerCountToPass = 3;
    private static final String fileName = "test-questions.csv";

    @MockBean
    private AppProperties fileNameProvider;

    @Autowired
    private CsvQuestionDao dao;


    @Test
    public void success_reading_questions_from_file_test() {
        // given
        int questionCountExpected = 3;
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn(fileName);
        Mockito.when(fileNameProvider.getRightAnswersCountToPass()).thenReturn(rightAnswerCountToPass);
        // when
        List<Question> questions = dao.findAll();
        // then
        Assertions.assertTrue(questions != null && !questions.isEmpty());
        Assertions.assertEquals(questionCountExpected, questions.size());
    }

    @Test
    public void not_exists_file_throw_QuestionReadException_test() {
        // given
        String emptyFileName = "not-exists-file.csv";
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn(emptyFileName);
        Mockito.when(fileNameProvider.getRightAnswersCountToPass()).thenReturn(rightAnswerCountToPass);
        // then
        Assertions.assertThrows(QuestionReadException.class, dao::findAll);
    }

}
