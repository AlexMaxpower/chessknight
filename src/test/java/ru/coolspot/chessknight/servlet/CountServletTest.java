package ru.coolspot.chessknight.servlet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountServletTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnCorrectValue() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +
                        "/horse/servlet/count?width=10&height=14&start=B1&end=A3",
                String.class)).isEqualTo("1\r\n");
    }

    @Test
    public void greetingShouldReturnErrorMessage() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +
                        "/horse/servlet/count?width=0&height=14&start=B1&end=A3",
                String.class)).contains("Error");
    }
}