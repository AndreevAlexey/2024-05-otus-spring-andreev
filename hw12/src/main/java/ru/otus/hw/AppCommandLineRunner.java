package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.security.AppUserService;


@Component
@RequiredArgsConstructor
public class AppCommandLineRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final AppUserService appUserService;

    @Override
    public void run(String... args) throws Exception {

        encodeUsersPassword();

    }

    private void encodeUsersPassword() {
        appUserService.findAll()
                .stream()
                .peek(user -> {
                    String password = user.getPassword();
                    String newPassword = passwordEncoder.encode(password);
                    user.setPassword(newPassword);
                })
                .forEach(appUserService::update);
    }

}
