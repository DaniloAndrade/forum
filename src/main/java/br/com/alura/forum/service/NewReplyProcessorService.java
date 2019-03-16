package br.com.alura.forum.service;

import br.com.alura.forum.model.Answer;
import br.com.alura.forum.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewReplyProcessorService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ForumMailService forumMailService;


    public void execute(Answer answer) {

        answerRepository.save(answer);
        forumMailService.sendEmail(answer);
    }

}
