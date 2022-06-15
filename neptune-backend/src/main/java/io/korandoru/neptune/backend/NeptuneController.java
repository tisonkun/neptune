/*
 * Copyright 2022 Korandoru Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.korandoru.neptune.backend;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NeptuneController {

    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public NeptuneController(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostConstruct
    public void construct() {
        System.out.println("construct");

        final var parameters = new MapSqlParameterSource();
        parameters.addValue("existence", List.of("apache/pulsar", "apache/kafka"));

        final var result = this.jdbc.queryForList("""
    SELECT
        repo_name,
        count() AS stars
    FROM github_events
    WHERE (event_type = 'WatchEvent') AND (actor_login IN
    (
        SELECT actor_login
        FROM github_events
        WHERE (event_type = 'WatchEvent') AND (repo_name IN (:existence))
    )) AND (repo_name NOT IN (:existence))
    GROUP BY repo_name
    ORDER BY stars DESC
    LIMIT 50
""", parameters);
        System.out.println(result);
    }

}
