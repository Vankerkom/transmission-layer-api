package be.vankerkom.transmissionlayer.components;

import be.vankerkom.transmissionlayer.config.AdminConfigurationProperties;
import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DevelopmentAdminSeederComponent implements ApplicationRunner {

    private final Logger LOG = LogManager.getLogger(getClass());

    @Autowired
    private AdminConfigurationProperties adminConfigurationProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!adminConfigurationProperties.isEnabled()) {
            return;
        }

        if (userRepository.count() != 0) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating admin account...");
        }

        try {
            final User admin = new User();

            admin.setUsername(adminConfigurationProperties.getUsername());
            admin.setPassword(passwordEncoder.encode(adminConfigurationProperties.getPassword()));
            admin.setEmail("");

            userRepository.save(admin);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Admin account: {} created", admin.getUsername());
            }

        } catch (Exception e) {
            LOG.error("Failed to create admin account", e);
        }

    }

}
