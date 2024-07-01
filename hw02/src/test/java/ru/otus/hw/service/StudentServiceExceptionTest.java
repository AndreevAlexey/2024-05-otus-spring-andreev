package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class StudentServiceExceptionTest {

    @Mock
    private static StreamsIOService ioService;

    @InjectMocks
    private static StudentServiceImpl studentService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
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
