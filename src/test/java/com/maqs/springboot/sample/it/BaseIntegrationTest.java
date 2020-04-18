package com.maqs.springboot.sample.it;

import com.maqs.springboot.sample.ApplicationRunner;
import com.maqs.springboot.sample.BaseTest;
import com.maqs.springboot.sample.model.Person;
import com.maqs.springboot.sample.util.Util;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = ApplicationRunner.class
)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public abstract class BaseIntegrationTest extends BaseTest {

    public BaseIntegrationTest() { }
}
