package com.maqs.springboot.sample.it.repository;

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
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Slf4j
public class PersonRepoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    private static boolean setupDone = false;

    @Before
    public  void setUpDatabase() {
        if (! setupDone) {
            String file = "/persons.json";
            log.debug("creating records of type: Person from file " + file);
            List<Person> list = readFile(file, Person.class);
            for (Person r: list) {
                personRepository.saveAndFlush(r);
            }
            setupDone = true;
        }
    }

    @Test
    public void testPersonList_FirstPageOfSizeTwo() {
        System.out.println("testPersonList_FirstPageOfSizeTwo");
        Pageable pageable = Util.getPageRequest(null, 0, 2);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Person> page = personRepository.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
    }

    @Test
    public void testTestModelList_SortedByAgeInAscendingOrder() {
        Pageable pageable = Util.getPageRequest("age", 0, 10);
        Page<Person> page = personRepository.findAll(pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isTrue();
        List<Person> list = page.getContent();
        Assertions.assertThat(Util.isInOrder("age", list, true)).isTrue();
    }

    @Test
    public void testTestModelList_SortedByAgeInDescendingOrder() {
        Pageable pageable = Util.getPageRequest("-age", 0, 10);
        Page<Person> page = personRepository.findAll(pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isTrue();
        List<Person> list = page.getContent();
        Assertions.assertThat(Util.isInOrder("age", list, false)).isTrue();
    }

    @Test
    public void testTestModelList_whereAgeIs30() {
        SearchCriteria criteria = new SearchCriteria();
        SearchCriteria.Filter ageFilter = new SearchCriteria.Filter("age", 30);
        criteria.addFilter(ageFilter);
        Pageable pageable = Pageable.unpaged();
        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
        Page<Person> page = personRepository.findAll(spec, pageable);

        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
        List<Person> list = page.getContent();

        Assertions.assertThat(Util.hasOnlyGivenCriteria("age", 30, list)).isTrue();
    }

    @Test
    public void testTestModelList_whereAgeIsGreaterThan100_noContent() {
        SearchCriteria criteria = new SearchCriteria();
        SearchCriteria.Filter ageFilter = new SearchCriteria.Filter("age", SearchCriteria.Operation.gt, 100);
        criteria.addFilter(ageFilter);
        Pageable pageable = Pageable.unpaged();
        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
        Page<Person> page = personRepository.findAll(spec, pageable);

        Assertions.assertThat(page.hasContent()).isFalse();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
    }

    @Test
    public void testTestModelList_whereAgeIsLessThan50_hasContent() {
        SearchCriteria criteria = new SearchCriteria();
        SearchCriteria.Filter ageFilter = new SearchCriteria.Filter("age", SearchCriteria.Operation.lt, 50);
        criteria.addFilter(ageFilter);
        Pageable pageable = Pageable.unpaged();
        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
        Page<Person> page = personRepository.findAll(spec, pageable);

        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
    }

    @Test
    public void testTestModelList_whereAgeIs50AndDobBetweenSomeRange_hasContent() {
        SearchCriteria criteria = new SearchCriteria();
        SearchCriteria.Filter ageFilter = new SearchCriteria.Filter("age", SearchCriteria.Operation.lt, 50);
        criteria.addFilter(ageFilter);

        SearchCriteria.Filter dateFilter = new SearchCriteria.Filter("dob", SearchCriteria.Operation.btw,
                new Long[] { 829725600000l, 829785600000l});
        criteria.addFilter(dateFilter);

        Pageable pageable = Pageable.unpaged();
        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
        Page<Person> page = personRepository.findAll(spec, pageable);

        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
    }

    @Test
    public void testTestModelList_whereAgeIsGreaterThan30AndDobBetweenSomeRange_sortedByAge() {
        SearchCriteria criteria = new SearchCriteria();
        SearchCriteria.Filter ageFilter = new SearchCriteria.Filter("age",30);
        criteria.addFilter(ageFilter);

        SearchCriteria.Filter dateFilter = new SearchCriteria.Filter("dob", SearchCriteria.Operation.btw,
                new Long[] { 829725600000l, 829785600000l});
        criteria.addFilter(dateFilter);

        Pageable pageable = Util.getPageRequest("age", 0, 10);
        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
        Page<Person> page = personRepository.findAll(spec, pageable);

        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isTrue();

        List<Person> list = page.getContent();
        Assertions.assertThat(Util.hasOnlyGivenCriteria("age", 30, list)).isTrue();
        Assertions.assertThat(Util.isInOrder("age", list, true)).isTrue();
    }
}
