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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.List;

@Slf4j
public class PersonRepoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    private static boolean setupDone = false;

    @Before
    public  void setUpDatabase() {
        if (! setupDone) {
            System.out.println("setUpDatabase");
            List<Person> list = readFile("/persons.json", Person.class);
            for (Person r: list) {
//                Integer age = r.getAge();
//                LocalDate date = LocalDate.now().minusYears(age);
//                long millis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
//                r.setDob(millis);
                personRepository.saveAndFlush(r);
            }
            System.out.println(Util.toJson(list));
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

//    @Test
//    public void testTestModelList_SortedByStatusInAscendingOrder() {
//        Pageable pageable = Util.getPageRequest("status", 0, 10);
//        Page<Person> page = personRepository.findAll(pageable);
//        Assertions.assertThat(page.hasContent()).isTrue();
//        Assertions.assertThat(page.isFirst()).isTrue();
//        Assertions.assertThat(page.getSort().isSorted()).isTrue();
//        List<Person> list = page.getContent();
//        Assertions.assertThat(Util.isInOrder("status", list, true)).isTrue();
//    }
//
//    @Test
//    public void testTestModelList_SortedByStatusInDescendingOrder() {
//        Pageable pageable = Util.getPageRequest("-status", 0, 10);
//        Page<Person> page = personRepository.findAll(pageable);
//        Assertions.assertThat(page.hasContent()).isTrue();
//        Assertions.assertThat(page.isFirst()).isTrue();
//        Assertions.assertThat(page.getSort().isSorted()).isTrue();
//        List<Person> list = page.getContent();
//        Assertions.assertThat(Util.isInOrder("status", list, false)).isTrue();
//    }
//
//    @Test
//    public void testTestModelList_whereStatusIs30() {
//        SearchCriteria criteria = new SearchCriteria();
//        SearchCriteria.Filter statusFilter = new SearchCriteria.Filter("status", 30);
//        criteria.addClause(statusFilter);
//        Pageable pageable = Pageable.unpaged();
//        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
//        Page<Person> page = personRepository.findAll(spec, pageable);
//
//        Assertions.assertThat(page.hasContent()).isTrue();
//        Assertions.assertThat(page.isFirst()).isTrue();
//        Assertions.assertThat(page.getSort().isSorted()).isFalse();
//        List<Person> list = page.getContent();
//
//        Assertions.assertThat(Util.hasOnlyGivenCriteria("status", 30, list)).isTrue();
//    }
//
//    @Test
//    public void testTestModelList_whereStatusIsGreaterThan30_noContent() {
//        SearchCriteria criteria = new SearchCriteria();
//        SearchCriteria.Filter statusFilter = new SearchCriteria.Filter("status", SearchCriteria.Operation.GT, 30);
//        criteria.addClause(statusFilter);
//        Pageable pageable = Pageable.unpaged();
//        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
//        Page<Person> page = personRepository.findAll(spec, pageable);
//
//        Assertions.assertThat(page.hasContent()).isFalse();
//        Assertions.assertThat(page.isFirst()).isTrue();
//        Assertions.assertThat(page.getSort().isSorted()).isFalse();
//    }
//
//    @Test
//    public void testTestModelList_whereStatusIsLessThan30_hasContent() {
//        SearchCriteria criteria = new SearchCriteria();
//        SearchCriteria.Filter statusFilter = new SearchCriteria.Filter("status", SearchCriteria.Operation.LT, 30);
//        criteria.addClause(statusFilter);
//        Pageable pageable = Pageable.unpaged();
//        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
//        Page<Person> page = personRepository.findAll(spec, pageable);
//
//        Assertions.assertThat(page.hasContent()).isTrue();
//        Assertions.assertThat(page.isFirst()).isTrue();
//        Assertions.assertThat(page.getSort().isSorted()).isFalse();
//    }
//
//    @Test
//    public void testTestModelList_whereStatusIs30AndtimesheetDateBetweenSomeRange_hasContent() {
//        SearchCriteria criteria = new SearchCriteria();
//        SearchCriteria.Filter statusFilter = new SearchCriteria.Filter("status", SearchCriteria.Operation.LT, 30);
//        criteria.addClause(statusFilter);
//
//        SearchCriteria.Filter dateFilter = new SearchCriteria.Filter("timesheetDate", SearchCriteria.Operation.BTW,
//                new Date[] { new Date(1541611400000l), new Date(1544639400000l)});
//        criteria.addClause(dateFilter);
//
//        Pageable pageable = Pageable.unpaged();
//        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
//        Page<Person> page = personRepository.findAll(spec, pageable);
//
//        Assertions.assertThat(page.hasContent()).isTrue();
//        Assertions.assertThat(page.isFirst()).isTrue();
//        Assertions.assertThat(page.getSort().isSorted()).isFalse();
//    }
//
//    @Test
//    public void testTestModelList_whereStatusIs30AndtimesheetDateBetweenSomeRange_sortedByStatus() {
//        SearchCriteria criteria = new SearchCriteria();
//        SearchCriteria.Filter statusFilter = new SearchCriteria.Filter("status", 30);
//        criteria.addClause(statusFilter);
//
//        SearchCriteria.Filter dateFilter = new SearchCriteria.Filter("timesheetDate", SearchCriteria.Operation.BTW,
//                new Date[] { new Date(1511639400000l), new Date(1545639400000l)});
//        criteria.addClause(dateFilter);
//
//        Pageable pageable = Util.getPageRequest("status", 0, 10);
//        Specification<Person> spec = SpecificationBuilder.findBy(criteria);
//        Page<Person> page = personRepository.findAll(spec, pageable);
//
//        Assertions.assertThat(page.hasContent()).isTrue();
//        Assertions.assertThat(page.isFirst()).isTrue();
//        Assertions.assertThat(page.getSort().isSorted()).isTrue();
//
//        List<Person> list = page.getContent();
//        Assertions.assertThat(Util.hasOnlyGivenCriteria("status", 30, list)).isTrue();
//        Assertions.assertThat(Util.isInOrder("status", list, true)).isTrue();
//    }
}
