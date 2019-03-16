package br.com.alura.forum.validator;


import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.PossibleSpam;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NewTopicCustomValidator implements Validator {

    private CourseRepository courseRepository;
    private TopicRepository topicRepository;
    private User loggedUser;


    public NewTopicCustomValidator(CourseRepository courseRepository, TopicRepository topicRepository, User loggedUser) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
        this.loggedUser = loggedUser;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return NewTopicInputDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        List<Topic> topics = topicRepository
                .findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(loggedUser, oneHourAgo);

        PossibleSpam possibleSpam = new PossibleSpam(topics);
        if (possibleSpam.hasTopicLimitExceeded()){
            errors.reject("newTopicInputDto.limit.exceeded",
                    new Object[]{possibleSpam.minutesToNextTopic(oneHourAgo)},
                    "O limite individual de novos tópicos por hora foi excedido!");
        }


        NewTopicInputDto newTopic = (NewTopicInputDto) target;
        Course course = courseRepository.findByName(newTopic.getCourseName());
        if (course == null){
            errors.rejectValue("courseName",
                    "newTopicInputDto.courseName.not.exists",
                    new Object[]{newTopic.getCourseName()},
                    "O Nome do curso não foi encontrado!");
        }

    }
}
