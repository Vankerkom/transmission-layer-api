package be.vankerkom.transmissionlayer.components;

import be.vankerkom.transmissionlayer.config.AdminConfigurationProperties;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DevelopmentAdminSeederComponent implements ApplicationRunner {

    private final AdminConfigurationProperties adminConfigurationProperties;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!adminConfigurationProperties.isEnabled()) {
            return;
        }

        if (userRepository.count() != 0) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Creating admin account...");
        }

        try {
            final User admin = new User();

            admin.setUsername(adminConfigurationProperties.getUsername());
            admin.setPassword(passwordEncoder.encode(adminConfigurationProperties.getPassword()));
            admin.setAdmin(true);
            admin.setEmail("");

            userRepository.save(admin);

            if (log.isDebugEnabled()) {
                log.debug("Admin account: {} created", admin.getUsername());
            }

        } catch (Exception e) {
            log.error("Failed to create admin account", e);
        }

    }

}
