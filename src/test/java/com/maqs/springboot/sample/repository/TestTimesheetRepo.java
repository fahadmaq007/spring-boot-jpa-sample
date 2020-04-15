package com.maqs.springboot.sample.repository;

import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.model.Timesheet;
import com.maqs.springboot.sample.util.Util;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

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
        Assert.assertTrue(page.hasContent());
        Assert.assertTrue(page.isFirst());
        Assert.assertFalse(page.getSort().isSorted());
    }

    @Test
    public void testTimesheetList_SortedByStatusInAscendingOrder() {
        Pageable pageable = Util.getPageRequest("status", 0, 2);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Timesheet> page = timesheetRepo.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assert.assertTrue(page.hasContent());
        Assert.assertTrue(page.isFirst());
        Assert.assertTrue(page.getSort().isSorted());
    }

    @Test
    public void testTimesheetList_SortedByStatusInDescendingOrder() {
        Pageable pageable = Util.getPageRequest("-status", 0, 2);
        SearchCriteria searchCriteria = new SearchCriteria();

        Page<Timesheet> page = timesheetRepo.findAll(SpecificationBuilder.findBy(searchCriteria), pageable);
        Assert.assertTrue(page.hasContent());
        Assert.assertTrue(page.isFirst());
        Assert.assertTrue(page.getSort().isSorted());
    }
}
