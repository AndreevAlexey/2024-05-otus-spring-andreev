package ru.otus.hw.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.domain.Student;


public class StudentServiceTest {

    @Mock
    private LocalizedIOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void success_determineCurrentStudent_test() {
        //given
        String expectedFirstName = "Ivan";
        String expectedLastName = "Petrov";
        Mockito.when(ioService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn("Ivan");
        Mockito.when(ioService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn("Petrov");
        //when
        Student student = studentService.determineCurrentStudent();
        //then
        Assertions.assertNotNull(student);
        Assertions.assertEquals(expectedFirstName, student.firstName());
        Assertions.assertEquals(expectedLastName, student.lastName());
    }

}
