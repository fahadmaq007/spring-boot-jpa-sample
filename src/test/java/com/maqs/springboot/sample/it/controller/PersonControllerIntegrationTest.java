package com.maqs.springboot.sample.it.controller;

import com.maqs.springboot.sample.it.BaseIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class PersonControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListPersons_postCriteria() throws Exception {
        String content = "{\"filters\":[{\"field\":\"age\",\"op\":\"EQ\",\"value\":30}]}";
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/persons/json")
                        .contentType(JSON).content(content)
                        .accept(JSON))
                .andReturn();

        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testListPersons_postCriteria_failInvalidFilterEQ1() throws Exception {
        String content = "{\"filters\":[{\"field\":\"age\",\"op\":\"EQ1\",\"value\":30}]}";
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/persons/json")
                        .contentType(JSON).content(content)
                        .accept(JSON))
                .andReturn();
        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
