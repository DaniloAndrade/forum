package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.NewAnswerInputDto;
import br.com.alura.forum.controller.dto.output.AnswerOutputDto;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.AnswerRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.NewReplyProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/topics/{topicId}/answers")
public class AnswerController {


    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private NewReplyProcessorService newReplyProcessorService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerOutputDto> answerTopic(@PathVariable Long topicId,
                                                       @Valid @RequestBody NewAnswerInputDto newAnswerDto,
                                                       @AuthenticationPrincipal User loggedUser,
                                                       UriComponentsBuilder uriBuilder) {

        Topic topic = this.topicRepository.findById(topicId).get();
        Answer answer = newAnswerDto.build(topic, loggedUser);

        newReplyProcessorService.execute(answer);

        URI path = uriBuilder
                .path("/api/topics/{topicId}/answers/{answer}")
                .buildAndExpand(topicId, answer.getId())
                .toUri();

        return ResponseEntity.created(path).body(new AnswerOutputDto(answer));
    }

    @GetMapping(value = "/{answerId}")
    public ResponseEntity<AnswerOutputDto> findAnswerTopic(@PathVariable Long topicId, @PathVariable Long answerId) {
        Answer answer = answerRepository.findById(answerId);
        return ResponseEntity.ok(new AnswerOutputDto(answer));
    }

}
