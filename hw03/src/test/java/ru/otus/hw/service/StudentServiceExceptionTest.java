package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentServiceExceptionTest {

    @Mock
    private StreamsIOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;


    @Test
    public void scanner_exception_throw_determineCurrentStudent_test() {
        //given
        String errorMessage = "some problem with scanner";
        //then
        Assertions.assertThrows(RuntimeException.class, () -> studentService.determineCurrentStudent(), errorMessage);
    }
}
