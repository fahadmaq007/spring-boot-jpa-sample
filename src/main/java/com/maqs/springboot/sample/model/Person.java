package com.maqs.springboot.sample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Person")
@Table(name = "person")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = true)
    private Boolean isActive;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Column(name = "dob", nullable = true)
    private Long dob;

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "company", nullable = true)
    private String company;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "address", nullable = true)
    private String address;
}
