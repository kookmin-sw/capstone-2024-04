package com.advt.server.domain.test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Test {
    @Id
    private Long testId;
}
