package br.com.alura.forum.controller;


import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.ItemDashboardOutputDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.controller.dto.output.TopicDetailOutputDto;
import br.com.alura.forum.controller.dto.output.TopicOutputDto;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.validator.NewTopicCustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTopic(@RequestBody @Valid NewTopicInputDto newTopic,
                                         @AuthenticationPrincipal User userLogado,
                                         UriComponentsBuilder uriBuilder) {

        Topic topic = newTopic.build(userLogado, courseRepository::findByName);
        topicRepository.save(topic);
        URI uri = uriBuilder.path("/api/topics/{id}").buildAndExpand(topic.getId()).toUri();

        return ResponseEntity.created(uri).body(new TopicOutputDto(topic));
    }



    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopicDetailOutputDto> findTopic(@PathVariable Long id){
       return topicRepository.findById(id).map(TopicDetailOutputDto::create)
                .map(t -> ResponseEntity.ok(t))
                .orElse(ResponseEntity.notFound().build());
    }


    @InitBinder("newTopicInputDto")
    public void initBinder(WebDataBinder dataBinder, @AuthenticationPrincipal User loggedUser) {
        dataBinder.addValidators(new NewTopicCustomValidator(courseRepository, topicRepository, loggedUser));
    }
}
