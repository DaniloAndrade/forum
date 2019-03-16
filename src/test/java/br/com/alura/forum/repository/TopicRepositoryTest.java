package br.com.alura.forum.repository;

import br.com.alura.forum.model.OpenTopicsByCategory;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.setup.TopicRepositoryTestsSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TopicRepositoryTest {


    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TopicRepository topicRepository;


    @Test
    public void shouldSaveATopic() {
        Topic topic = new Topic("Descrição do tópico", "Conteúdo do tópico", null, null);
        Topic persistedTopic = this.topicRepository.save(topic);
        Topic foundTopic = this.testEntityManager.find(Topic.class, persistedTopic.getId());

        assertThat(foundTopic).isNotNull();

        assertThat(foundTopic.getShortDescription())
                .isEqualTo(persistedTopic.getShortDescription());
    }


    @Test
    public void shouldReturnOpenTopicsByCategory() {
        TopicRepositoryTestsSetup testSetup = new TopicRepositoryTestsSetup(testEntityManager);
        testSetup.openTopicsByCategorySetup();

        List<OpenTopicsByCategory> openTopics = this.topicRepository.findOpenTopicsByCategory();

        assertThat(openTopics).isNotEmpty();
        assertThat(openTopics).hasSize(2)
                .extracting("categoryName", "topicCount")
                .contains(
                        tuple("Programação", 2),
                        tuple("Front-end", 1)
                );

    }
    // Programação
    // Programação

}