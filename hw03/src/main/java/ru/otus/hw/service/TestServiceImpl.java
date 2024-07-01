package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;



@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        for (var question: questions) {
            var questionResult = processQuestion(question);
            testResult.applyAnswer(question, questionResult);
        }
        return testResult;
    }

    private boolean processQuestion(Question question) {
        printQuestion(question);
        var answers = question.answers();
        printAnswers(answers);
        int chosenAnswer = chooseAnswer(answers.size());
        return checkAnswer(answers, chosenAnswer);
    }

    private int chooseAnswer(int max) {
        return ioService.readIntForRangeWithPromptLocalized(1, max, "TestService.input.answer", "TestService.wrong.input");
    }

    private boolean checkAnswer(List<Answer> answers, int num) {
        return answers.get(num - 1).isCorrect();
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
    }

    private void printAnswers(List<Answer> answerList) {
        if (answerList != null && !answerList.isEmpty()) {
            for (int i = 0; i < answerList.size(); i++) {
                ioService.printLine((i + 1) + ". " + answerList.get(i).text());
            }
        }
    }

}