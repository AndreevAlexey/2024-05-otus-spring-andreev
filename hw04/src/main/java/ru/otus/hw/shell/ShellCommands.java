package ru.otus.hw.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(key = "info", value = "Application info")
    public String info() {
        return "This is the Student Test Application";
    }

    @ShellMethod(key = "start-test", value = "Start test student")
    public void startTest() {
        testRunnerService.run();
    }
}
