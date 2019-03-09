package br.com.alura.forum.controller.dto.input;

import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.function.Function;

public class NewTopicInputDto {

    @NotBlank
    @Size(min = 10)
    private String shortDescription;

    @NotBlank
    @Size(min = 10)
    private String content;
    @NotBlank
    private String courseName;


    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Topic build(User owner, Function<String, Course> findByName) {
        Topic topic = new Topic(shortDescription, content, owner, findByName.apply(courseName));
        return topic;
    }
}
