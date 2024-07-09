package ru.otus.hw.shell;


import org.springframework.shell.standard.ShellComponent;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@ShellComponent
public class ConsoleCommands {

    @ShellMethod(key = "h2-console", value = "Open H2 console")
    public void openConsole() throws SQLException {
        Console.main();
    }
}
