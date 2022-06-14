package io.korandoru.neptune.backend;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootTest
class NeptuneBackendApplicationTests {

    @Bean
    public DataSource dataSource() {
        final var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db");
        dataSource.setUsername("neptune");
        return dataSource;
    }

    @Test
    void contextLoads() {
    }

}
