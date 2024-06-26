package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class StudentServiceExceptionTest {

    private static StreamsIOService ioService;

    private static StudentServiceImpl studentService;

    @BeforeAll
    static void init() {
        ioService = Mockito.mock(StreamsIOService.class);
        studentService = new StudentServiceImpl(ioService);
    }

    @Test
    public void scanner_exception_throw_determineCurrentStudent_test() {
        //given
        String errorMessage = "some problem with scanner";
        Mockito.when(ioService.readStringWithPrompt("Please input your first name")).thenReturn("Ivan");
        Mockito.when(ioService.readStringWithPrompt("Please input your last name")).thenThrow(new RuntimeException(errorMessage));
        //then
        Assertions.assertThrows(RuntimeException.class, () -> studentService.determineCurrentStudent(), errorMessage);
    }
}
