package ru.otus.hw;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hw.service.StreamsIOService;

import java.io.PrintStream;

public class StreamsIOServiceTest {
    private static PrintStream printStream;
    private static StreamsIOService ioService;

    @BeforeAll
    static void init() {
        printStream = System.out;
        ioService = new StreamsIOService(printStream);
    }

    @Test
    public void test_success_print() {
        //given
        String testLine = "test";
        //when
        ioService.printLine(testLine);
        //then
    }
}
