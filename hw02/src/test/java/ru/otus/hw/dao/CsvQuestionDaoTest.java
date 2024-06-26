package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;


public class CsvQuestionDaoTest {

    private static final int rightAnswerCountToPass = 3;
    private static final String fileName = "test-questions.csv";

    private static CsvQuestionDao dao = null;
    private static TestFileNameProvider fileNameProvider = null;

    @BeforeAll
    public static void setUp() {
        fileNameProvider = new AppProperties(rightAnswerCountToPass, fileName);
        dao = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    public void success_reading_questions_from_file_test() {
        // given
        int questionCountExpected = 3;
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
        fileNameProvider = new AppProperties(rightAnswerCountToPass, emptyFileName);
        CsvQuestionDao dao = new CsvQuestionDao(fileNameProvider);
        // then
        Assertions.assertThrows(QuestionReadException.class, dao::findAll);
    }

}
