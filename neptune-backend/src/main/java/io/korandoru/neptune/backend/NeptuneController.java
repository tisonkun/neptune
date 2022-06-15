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

import io.korandoru.neptune.backend.query.StargazersCrossQuery;
import io.korandoru.neptune.backend.query.StargazersCrossRequest;
import io.korandoru.neptune.backend.query.StargazersCrossResult;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NeptuneController {

    private final StargazersCrossQuery stargazersCrossQuery;

    @Autowired
    public NeptuneController(StargazersCrossQuery stargazersCrossQuery) {
        this.stargazersCrossQuery = stargazersCrossQuery;
    }

    @RequestMapping("/stargazers_cross")
    @ResponseBody
    public StargazersCrossResult stargazersCross(@RequestBody StargazersCrossRequest request) {
        return this.stargazersCrossQuery.doQuery(request.origins());
    }

}
