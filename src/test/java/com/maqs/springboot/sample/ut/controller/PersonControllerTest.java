package com.maqs.springboot.sample.ut.controller;

import com.maqs.springboot.sample.BaseTest;
import com.maqs.springboot.sample.dto.SearchCriteria;
import com.maqs.springboot.sample.model.Person;
import com.maqs.springboot.sample.services.PersonService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class PersonControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void test() throws Exception {
        Page<Person> expectedPage = Page.empty();
        Mockito.when(
                personService.list(any(SearchCriteria.class), anyString(), anyInt(), anyInt()))
                .thenReturn(expectedPage);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/persons")
                .accept(JSON))
                .andReturn();

        Mockito.verify(personService).list(null, null, null, null);
        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
