package com.maqs.springboot.sample.ut.repository;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.it.BaseIntegrationTest;
import com.maqs.springboot.sample.model.Person;
import com.maqs.springboot.sample.repository.PersonRepository;
import com.maqs.springboot.sample.repository.SpecificationBuilder;
import com.maqs.springboot.sample.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
public class TestPersonRepo extends BaseIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    private static boolean setupDone = false;

    @Before
    public  void setUpDatabase() {
        if (! setupDone) {
            String file = "/persons.json";
            log.debug("creating records of type: Person from file " );
            List<Person> list = readFile(file, Person.class);
            for (Person r: list) {
                personRepository.saveAndFlush(r);
            }
            setupDone = true;
        }
    }

    @Test
    public void testPersonList_FirstPageOfSizeTwo() {
        Pageable pageable = Util.getPageRequest(null, 0, 25);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Person> page = personRepository.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();

        System.out.println(Util.toJson(page.getContent()));
    }

    @Test
    public void testPersonList_SortedByAgeInAscendingOrder() {
        Pageable pageable = Util.getPageRequest("age", 0, 10);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Person> page = personRepository.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isTrue();
        List<Person> list = page.getContent();
        Assertions.assertThat(Util.isInOrder("age", list, true)).isTrue();
    }

    @Test
    public void testPersonList_SortedByAgeInDescendingOrder() {
        Pageable pageable = Util.getPageRequest("-age", 0, 10);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Person> page = personRepository.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isTrue();
        List<Person> list = page.getContent();
        Assertions.assertThat(Util.isInOrder("age", list, false)).isTrue();
    }

    @Test
    public void testPersonList_hasOnlyAge30() {
        Pageable pageable = Util.getPageRequest(null, 0, 10);
        String field = "age";
        Integer value = 30;
        SearchCriteria searchCriteria = new SearchCriteria();
        SearchCriteria.Filter age30 = new SearchCriteria.Filter(field, value);
        searchCriteria.addClause(age30);

        Page<Person> page = personRepository.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
        List<Person> list = page.getContent();
        Assertions.assertThat(Util.hasOnlyGivenCriteria(field, value, list)).isTrue();
    }

    @Test
    public void testPersonList_hasOnlyAge30_butLookFor35_negativeTest() {
        Pageable pageable = Util.getPageRequest(null, 0, 10);
        String field = "age";
        Integer value = 30;
        SearchCriteria searchCriteria = new SearchCriteria();
        SearchCriteria.Filter age30 = new SearchCriteria.Filter(field, value);
        searchCriteria.addClause(age30);

        Page<Person> page = personRepository.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
        List<Person> list = page.getContent();
        Assertions.assertThat(Util.hasOnlyGivenCriteria(field, 35, list)).isFalse();
    }
}
