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
package software.plusminus.crud.service.meta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import software.plusminus.crud.TestCrudRepository;
import software.plusminus.crud.TestEntity;
import software.plusminus.crud.repository.CrudRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringCrudRepositoryContextIntegrationTest {

    @Autowired
    private SpringCrudRepositoryContext service;

    @Test
    public void findCrudRepository() throws Exception {
        CrudRepository repository = service.findCrudRepository(TestEntity.class);
        assertThat(repository).isNotNull();
        assertThat(repository).isInstanceOf(TestCrudRepository.class);
    }
}