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

package io.korandoru.neptune.backend.clickhouse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import org.springframework.data.jdbc.repository.config.DialectResolver;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.jdbc.core.JdbcOperations;

public class ClickHouseDialectProvider implements DialectResolver.JdbcDialectProvider {

    @Override
    public Optional<Dialect> getDialect(JdbcOperations operations) {
        return Optional.ofNullable(operations.execute(this::resolveDialect));
    }

    private Dialect resolveDialect(Connection connection) throws SQLException {
        final var meta = connection.getMetaData();
        final var name = meta.getDatabaseProductName().toLowerCase(Locale.ENGLISH);
        if (name.contains("clickhouse")) {
            return ClickhouseDialect.INSTANCE;
        }
        return null;
    }

}
