package com.maqs.springboot.sample.it.repository;

import com.maqs.springboot.sample.it.BaseIntegrationTest;
import com.maqs.springboot.sample.model.Person;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class PersonControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        Page<Person> expectedPage = Page.empty();
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/timesheets")
                        .accept(JSON))
                .andReturn();

        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
