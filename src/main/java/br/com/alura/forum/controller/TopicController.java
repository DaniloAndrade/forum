package br.com.alura.forum.controller;


import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.ItemDashboardOutputDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.model.topic.domain.TopicStatus;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TopicBriefOutputDto> topics(TopicSearchInputDto filter, @PageableDefault(sort = "creationInstant", direction = Sort.Direction.DESC) Pageable pageRequest) {
        Specification<Topic> specification = filter.buildSpecification();
        return TopicBriefOutputDto.listFrom(topicRepository.findAll(specification, pageRequest));
    }


    @GetMapping(value = "/dashboard",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemDashboardOutputDto> dashboard() {
        Instant lastWeek = Instant.now().minus(7, ChronoUnit.DAYS);
        return categoryRepository
                .findAllByCategoryIsNull().stream()
                .map(category -> {
                    List<Topic> topic = topicRepository.findAllByCategory(category);
                    return ItemDashboardOutputDto.createNew(category, topic, lastWeek);
                })
                 .collect(Collectors.toList());



    }
}
