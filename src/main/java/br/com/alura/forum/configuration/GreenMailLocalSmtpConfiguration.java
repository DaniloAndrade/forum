package br.com.alura.forum.configuration;


import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Profile("dev")
@Configuration
public class GreenMailLocalSmtpConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreenMailLocalSmtpConfiguration.class);

    private GreenMail smtpServer;

    @Value("${spring.mail.host}")
    private String hostAddress;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;


    @PostConstruct
    public void setup() {
        LOGGER.info("####################### Inicializando GreenMail SMTP");
        ServerSetup serverSetup = new ServerSetup(Integer.parseInt(port), this.hostAddress,"smtp");
        this.smtpServer = new GreenMail(serverSetup);

        this.smtpServer.setUser(username, username, password);
        this.smtpServer.start();
    }


    @PreDestroy
    public void destroy() {
        LOGGER.info("####################### Parando GreenMail SMTP");
        this.smtpServer.stop();
    }

}
