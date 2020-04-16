package com.maqs.springboot.sample.repository;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.model.Timesheet;
import com.maqs.springboot.sample.util.Util;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ContextConfiguration(classes = {H2JpaConfig.class})
//@TestPropertySource(locations="classpath:application.yml")
public class TestTimesheetRepo {

    @Autowired
    private TimesheetRepo timesheetRepo;

    @Test
    public void testTimesheetList_FirstPageOfSizeTwo() {
        Pageable pageable = Util.getPageRequest(null, 0, 2);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Timesheet> page = timesheetRepo.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
    }

    @Test
    public void testTimesheetList_SortedByStatusInAscendingOrder() {
        Pageable pageable = Util.getPageRequest("status", 0, 10);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Timesheet> page = timesheetRepo.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isTrue();
        List<Timesheet> list = page.getContent();
        Assertions.assertThat(Util.isInOrder("status", list, true)).isTrue();
    }

    @Test
    public void testTimesheetList_SortedByStatusInDescendingOrder() {
        Pageable pageable = Util.getPageRequest("-status", 0, 10);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Timesheet> page = timesheetRepo.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isTrue();
        List<Timesheet> list = page.getContent();
        Assertions.assertThat(Util.isInOrder("status", list, false)).isTrue();
    }

    @Test
    public void testTimesheetList_hasOnlyStatus30() {
        Pageable pageable = Util.getPageRequest(null, 0, 10);
        String field = "status";
        Integer value = 30;
        SearchCriteria searchCriteria = new SearchCriteria();
        SearchCriteria.Clause status30 = new SearchCriteria.Clause(field, value);
        searchCriteria.addClause(status30);

        Page<Timesheet> page = timesheetRepo.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
        List<Timesheet> list = page.getContent();
        Assertions.assertThat(Util.hasOnlyGivenCriteria(field, value, list)).isTrue();
    }

    @Test
    public void testTimesheetList_hasOnlyStatus30_butLookFor35_negativeTest() {
        Pageable pageable = Util.getPageRequest(null, 0, 10);
        String field = "status";
        Integer value = 30;
        SearchCriteria searchCriteria = new SearchCriteria();
        SearchCriteria.Clause status30 = new SearchCriteria.Clause(field, value);
        searchCriteria.addClause(status30);

        Page<Timesheet> page = timesheetRepo.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assertions.assertThat(page.hasContent()).isTrue();
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.getSort().isSorted()).isFalse();
        List<Timesheet> list = page.getContent();
        Assertions.assertThat(Util.hasOnlyGivenCriteria(field, 35, list)).isFalse();
    }
}
