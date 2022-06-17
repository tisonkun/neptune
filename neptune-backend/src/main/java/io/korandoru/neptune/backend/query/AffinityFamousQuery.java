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

package io.korandoru.neptune.backend.query;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AffinityFamousQuery {

    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public AffinityFamousQuery(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public AffinityFamousResult doQuery(List<String> origins) {
        log.debug("AffinityFamousQuery.doQuery with origins: {}", origins);

        final var parameters = new MapSqlParameterSource();
        parameters.addValue("origins", origins);

        final var result = this.jdbc.queryForList("""
                SELECT
                    repo_name,
                    count() AS stars
                FROM github_events
                WHERE (event_type = 'WatchEvent') AND (actor_login IN
                (
                    SELECT actor_login
                    FROM github_events
                    WHERE (event_type = 'WatchEvent') AND (repo_name IN (:origins))
                )) AND (repo_name NOT IN (:origins))
                GROUP BY repo_name
                ORDER BY stars DESC
                LIMIT 50
            """, parameters);

        final var crosses = new ArrayList<AffinityFamousResult.Item>();
        for (var item : result) {
            crosses.add(new AffinityFamousResult.Item(
                (String) item.get("repo_name"),
                (long) item.get("stars"))
            );
        }
        return new AffinityFamousResult(origins, crosses);
    }

}
