package br.com.alura.forum.repository;

import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.OpenTopicsByCategory;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TopicRepository extends Repository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    @Query(value = "select t from Topic t")
    List<Topic> list();

    List<Topic> findAll();

    @Query("select t from Topic t join t.course c join c.subcategory sub where sub.category = :category")
    List<Topic> findAllByCategory(Category category);


    Topic save(Topic topic);

    Optional<Topic> findById(Long id);

    List<Topic> findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(User owner, Instant oneHourAgo);

    @Query("select new br.com.alura.forum.model.OpenTopicsByCategory( " +
            "t.course.subcategory.category.name as categoryName, " +
            "count(t) as topicCount, " +
            " now() as instant) from Topic t " +
            "where t.status = 'NOT_ANSWERED' " +
            "group by t.course.subcategory.category")
    List<OpenTopicsByCategory> findOpenTopicsByCategory();
}
