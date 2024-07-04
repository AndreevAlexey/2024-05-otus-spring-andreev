package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    private static final String fileName = "test-questions.csv";

    @Mock
    private AppProperties fileNameProvider;

    @InjectMocks
    private CsvQuestionDao dao;

    @Test
    public void success_reading_questions_from_file_test() {
        // given
        int questionCountExpected = 3;
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn(fileName);
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
        // then
        Assertions.assertThrows(QuestionReadException.class, dao::findAll);
    }

}
