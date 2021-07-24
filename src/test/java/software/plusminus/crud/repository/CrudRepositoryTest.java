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
package software.plusminus.crud.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import software.plusminus.check.util.JsonUtils;
import software.plusminus.crud.TestCrudRepository;
import software.plusminus.crud.TestEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static software.plusminus.check.Checks.check;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CrudRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TestCrudRepository repository;

    @Test
    public void save() {
        TestEntity entity = JsonUtils.fromJson("/json/test-entity.json", TestEntity.class);
        entity.setId(null);

        TestEntity result = repository.save(entity);

        TestEntity inDb = entityManager.find(TestEntity.class, 1L);
        check(result).is(entity);
        check(result).is(inDb);
    }

    @Test
    public void findById() {
        List<TestEntity> entities = readEntities();
        entities.stream()
                .peek(entity -> entity.setId(null))
                .forEach(entityManager::persist);

        Optional<TestEntity> result = repository.findById(2L);

        assertThat(result).isPresent();
        check(result).is(entities.get(1));
    }

    @Test
    public void findById_ForMissed() {
        List<TestEntity> entities = readEntities();
        entities.stream()
                .peek(entity -> entity.setId(null))
                .forEach(entityManager::persist);

        Optional<TestEntity> result = repository.findById(321L);

        assertThat(result).isNotPresent();
    }

    @Test
    public void findAll() {
        List<TestEntity> entities = readEntities();
        entities.stream()
                .peek(entity -> entity.setId(null))
                .forEach(entityManager::persist);

        Page<TestEntity> result = repository.findAll(PageRequest.of(1, 2));

        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.getContent().size()).isEqualTo(1);
        check(result.getContent().get(0)).is(entities.get(2));
    }

    @Test
    public void findAll_Unpaged() {
        List<TestEntity> entities = readEntities();
        entities.stream()
                .peek(entity -> entity.setId(null))
                .forEach(entityManager::persist);

        Page<TestEntity> result = repository.findAll(Pageable.unpaged());

        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent().size()).isEqualTo(3);
        check(result.getContent().get(0)).is(entities.get(0));
        check(result.getContent().get(1)).is(entities.get(1));
        check(result.getContent().get(2)).is(entities.get(2));
    }

    @Test
    public void delete() {
        List<TestEntity> entities = readEntities();
        entities.stream()
                .peek(entity -> entity.setId(null))
                .forEach(entityManager::persist);

        repository.delete(entities.get(1));

        assertThat(entityManager.find(TestEntity.class, 1L)).isNotNull();
        assertThat(entityManager.find(TestEntity.class, 2L)).isNull();
        assertThat(entityManager.find(TestEntity.class, 3L)).isNotNull();
    }
    
    private List<TestEntity> readEntities() {
        return JsonUtils.fromJsonList("/json/test-entities.json", TestEntity[].class);
    }
}