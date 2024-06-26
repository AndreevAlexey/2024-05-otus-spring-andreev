package ru.otus.hw.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.domain.Student;

public class StudentServiceTest {

    private static StreamsIOService ioService;

    private static StudentServiceImpl studentService;

    @BeforeAll
    static void init() {
        ioService = Mockito.mock(StreamsIOService.class);
        studentService = new StudentServiceImpl(ioService);
    }

    @Test
    public void success_determineCurrentStudent_test() {
        //given
        String expectedFirstName = "Ivan";
        String expectedLastName = "Petrov";

        Mockito.when(ioService.readStringWithPrompt("Please input your first name")).thenReturn("Ivan");
        Mockito.when(ioService.readStringWithPrompt("Please input your last name")).thenReturn("Petrov");
        //when
        Student student = studentService.determineCurrentStudent();
        //then
        Assertions.assertNotNull(student);
        Assertions.assertEquals(expectedFirstName, student.firstName());
        Assertions.assertEquals(expectedLastName, student.lastName());
    }

}
