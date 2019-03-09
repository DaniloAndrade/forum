package br.com.alura.forum.controller.dto.output;

import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.model.topic.domain.TopicStatus;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class TopicDetailOutputDto {


    private final Long id;
    private final String shortDescription;
    private final String content;
    private final TopicStatus status;
    private final Instant creationInstant;
    private final Instant lastUpdate;
    private final String ownerName;
    private final String courseName;
    private final String subcategoryName;
    private final String categoryName   ;
    private final Integer numberOfResponses;
    private final List<AnswerOutputDto> answers;

    public TopicDetailOutputDto(Topic topic) {
        this.id = topic.getId();
        this.shortDescription = topic.getShortDescription();
        this.content = topic.getContent();
        this.status = topic.getStatus();
        this.creationInstant = topic.getCreationInstant();
        this.lastUpdate = topic.getLastUpdate();
        this.courseName = topic.getCourse().getName();
        this.subcategoryName = topic.getCourse().getSubcategoryName();
        this.categoryName = topic.getCourse().getCategoryName();
        this.ownerName= topic.getOwnerName();
        this.numberOfResponses = topic.getNumberOfAnswers();
        this.answers = topic.getAnswers()
                .stream().map(AnswerOutputDto::new)
                .collect(Collectors.toList());
    }


    public Long getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getContent() {
        return content;
    }

    public TopicStatus getStatus() {
        return status;
    }

    public Instant getCreationInstant() {
        return creationInstant;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getNumberOfResponses() {
        return numberOfResponses;
    }

    public List<AnswerOutputDto> getAnswers() {
        return answers;
    }

    public static TopicDetailOutputDto create(Topic topic) {
        return new TopicDetailOutputDto(topic);
    }
}
