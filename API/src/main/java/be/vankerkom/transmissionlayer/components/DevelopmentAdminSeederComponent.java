package be.vankerkom.transmissionlayer.components;

import be.vankerkom.transmissionlayer.models.User;
import be.vankerkom.transmissionlayer.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentAdminSeederComponent implements ApplicationRunner {

    private final Logger LOG = LogManager.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() != 0) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating admin account...");
        }

        try {
            final User admin = new User();

            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("");

            userRepository.save(admin);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Admin account: {} created", admin.getUsername());
            }

        }catch (Exception e) {
            LOG.error("Failed to create admin account", e);
        }

    }

}
