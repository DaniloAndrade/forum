package br.com.alura.forum.controller.dto.output;

import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.model.topic.domain.TopicStatus;

import java.time.Instant;
import java.util.List;

public class ItemDashboardOutputDto {
    private String categoryName;
    private List<String> subcategories;
    private Long allTopics;
    private Long lastWeekTopics;
    private Long unansweredTopics;


    public ItemDashboardOutputDto(String categoryName, List<String> subcategories,
                                  Long allTopics, Long lastWeekTopics, Long unansweredTopics) {
        this.categoryName = categoryName;
        this.subcategories = subcategories;
        this.allTopics = allTopics;
        this.lastWeekTopics = lastWeekTopics;
        this.unansweredTopics = unansweredTopics;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public Long getAllTopics() {
        return allTopics;
    }

    public Long getLastWeekTopics() {
        return lastWeekTopics;
    }

    public Long getUnansweredTopics() {
        return unansweredTopics;
    }

    public static ItemDashboardOutputDto createNew(Category category, List<Topic> topics, Instant lastWeekInstant) {
        Long all = Long.valueOf(topics.size());
        Long lastWeek =  topics.stream().filter(t -> lastWeekInstant.isBefore( t.getCreationInstant())).count();
        Long unanswered = topics.stream().filter(t -> t.getStatus() == TopicStatus.NOT_ANSWERED).count();
        return new ItemDashboardOutputDto(category.getName(), category.getSubcategoryNames(), all, lastWeek, unanswered);
    }
}
