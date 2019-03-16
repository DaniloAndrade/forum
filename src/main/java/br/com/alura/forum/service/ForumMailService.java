package br.com.alura.forum.service;

import br.com.alura.forum.infra.NewReplyMailFactory;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.topic.domain.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ForumMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForumMailService.class);


    @Autowired
    private JavaMailSender sender;

    @Autowired
    private NewReplyMailFactory newReplyMailFactory;


    @Async
    public void sendEmail(Answer answer) {
        LOGGER.info("Enviando notificação de resposta para o topico: {}", answer.getTopic().getId());
        try {
            Topic topic = answer.getTopic();

            MimeMessagePreparator mimeMessagePreparator = (mm) -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mm);
                messageHelper.setTo(topic.getOwnerEmail());
                messageHelper.setSubject("Novo comentário em: "+ topic.getShortDescription());
                String content = newReplyMailFactory.generateNewReplyMailContent(answer);
                messageHelper.setText(content, true);
            };

            sender.send(mimeMessagePreparator);
        } catch (MailException e) {
            LOGGER.error("Não foi possivel envia email", e);
        }

    }
}
