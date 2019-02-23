package br.com.alura.forum.repository;

import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TopicRepository extends Repository<Topic, Long>, JpaSpecificationExecutor<Topic> {

    @Query(value = "select t from Topic t")
    List<Topic> list();

    List<Topic> findAll();

    @Query("select t from Topic t join t.course c join c.subcategory sub where sub.category = :category")
    List<Topic> findAllByCategory(Category category);



}
