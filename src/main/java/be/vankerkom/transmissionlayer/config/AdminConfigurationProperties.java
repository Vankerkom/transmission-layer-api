package be.vankerkom.transmissionlayer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties("admin")
@Getter
@Setter
public class AdminConfigurationProperties {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private boolean enabled;

}
