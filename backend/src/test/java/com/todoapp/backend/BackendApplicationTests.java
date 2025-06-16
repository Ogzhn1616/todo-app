package com.todoapp.backend;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Spring context loads test is disabled during local dev")
@SpringBootTest
class BackendApplicationTests {
	@Test
	void contextLoads() {
	}
}