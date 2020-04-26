package be.vankerkom.transmissionlayer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("admin")
@Getter
@Setter
public class AdminConfigurationProperties {

    private String username = "";
    private String password = "";
    private boolean enabled = false;
    private boolean importTorrents = false;

}
