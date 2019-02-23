package br.com.alura.forum.controller.dto.output;

import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.model.topic.domain.TopicStatus;
import org.springframework.data.domain.Page;

import java.time.Duration;
import java.time.Instant;

public class TopicBriefOutputDto {

    private Long id;
    private String shortDescription;
    private long secondsSinceLastUpdate;
    private String ownerName;
    private String courseName;
    private String subcategoryName;
    private String categoryName;
    private int numberOfResponses;
    private boolean solved;


    public TopicBriefOutputDto(Topic topic) {
        this.id = topic.getId();
        this.shortDescription = topic.getShortDescription();
        this.secondsSinceLastUpdate = getSecondsSince(topic.getLastUpdate());
        this.ownerName = topic.getOwnerName();
        this.courseName = topic.getCourse().getName();
        this.subcategoryName = topic.getCourse().getSubcategoryName();
        this.categoryName = topic.getCourse().getCategoryName();
        this.numberOfResponses = topic.getNumberOfAnswers();
        this.solved = TopicStatus.SOLVED == topic.getStatus();
    }

    public static Page<TopicBriefOutputDto> listFrom(Page<Topic> topics) {
        return topics.map(TopicBriefOutputDto::new);
    }

    private long getSecondsSince(Instant lastUpdate) {
        return Duration.between(lastUpdate, Instant.now()).getSeconds();
    }

    public Long getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public long getSecondsSinceLastUpdate() {
        return secondsSinceLastUpdate;
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

    public int getNumberOfResponses() {
        return numberOfResponses;
    }

    public boolean isSolved() {
        return solved;
    }
}
