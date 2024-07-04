package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String filename = fileNameProvider.getTestFileName();
        ClassPathResource resource = new ClassPathResource(filename);
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader)) {

            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(bufferedReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build();

            return
                    csvToBean.parse()
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();

        } catch (Exception exp) {
            throw new QuestionReadException(exp.getMessage());
        }
    }
}