/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.plusminus.crud;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.plusminus.crud.controller.CrudApi;

import static java.util.Collections.singletonList;

@RestController
@RequestMapping("/test")
@Profile("test")
public class DummyCrudController implements CrudApi<TestEntity, Long> {
    @Override
    public TestEntity getById(@PathVariable("id") Long id) {
        return new TestEntity();
    }

    @Override
    public Page<TestEntity> getPage(Pageable pageable) {
        return new PageImpl(singletonList(new TestEntity()));
    }

    @Override
    public TestEntity create(@RequestBody TestEntity object) {
        return new TestEntity();
    }

    @Override
    public TestEntity update(@RequestBody TestEntity object) {
        return new TestEntity();
    }

    @Override
    public TestEntity patch(@RequestBody TestEntity patch) {
        return new TestEntity();
    }

    @Override
    public void delete(@RequestBody TestEntity object) {
    }
}