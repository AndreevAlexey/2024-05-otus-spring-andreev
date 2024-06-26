package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;


@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questionList = questionDao.findAll();
        questionList.forEach(this::printQuestion);
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        printAnswers(question.answers());
    }

    private void printAnswers(List<Answer> answerList) {
        if (answerList != null && !answerList.isEmpty()) {
            for (Answer answer : answerList) {
                ioService.printLine(answer.text());
            }
        }
    }
}