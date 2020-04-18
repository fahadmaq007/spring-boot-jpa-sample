package com.maqs.springboot.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@ActiveProfiles("test")
public abstract class BaseTest {
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected MediaType JSON = MediaType.APPLICATION_JSON;
    protected <E> List<E> readFile(String file, Class<E> clazz) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(
                    BaseTest.class.getResource(file).toURI()));
            CollectionType javaType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, clazz);
            List<E> list = objectMapper.readValue(fileBytes, javaType);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
