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
package software.plusminus.crud.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import software.plusminus.check.util.JsonUtils;
import software.plusminus.crud.TestEntity;
import software.plusminus.crud.exception.CreateException;
import software.plusminus.crud.exception.NotFoundException;
import software.plusminus.crud.exception.PatchException;
import software.plusminus.crud.exception.UpdateException;
import software.plusminus.crud.model.Update;
import software.plusminus.crud.repository.CrudRepository;
import software.plusminus.patch.service.PatchService;

import java.util.Collections;
import java.util.Optional;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static software.plusminus.check.Checks.check;

@RunWith(MockitoJUnitRunner.class)
public class EntityCrudServiceTest {

    private static final String ENTITY = "entity";
    private static final String SAVED = "saved";

    @Mock
    private Validator validator;
    @Mock
    private PatchService patchService;
    @Mock
    private CrudRepository<TestEntity, Long> repository;
    @InjectMocks
    private EntityCrudService<TestEntity, Long> crudService = mock(EntityCrudService.class, Answers.CALLS_REAL_METHODS);
    @Captor
    private ArgumentCaptor<TestEntity> captor;

    @Test
    public void readById() {
        TestEntity entity = readEntity();
        when(repository.findById(2L)).thenReturn(Optional.of(entity));

        TestEntity result = crudService.getById(2L);

        assertThat(result).isSameAs(entity);
    }

    @Test(expected = NotFoundException.class)
    public void readById_NotFoundException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        crudService.getById(2L);
    }

    @Test
    public void readPage() {
        TestEntity entity = readEntity();
        Pageable pageable = PageRequest.of(2, 3);
        Page<TestEntity> page = new PageImpl<>(Collections.singletonList(entity), pageable, 100);
        when(repository.findAll(pageable)).thenReturn(page);

        Page<TestEntity> result = crudService.getPage(pageable);

        check(result).is(page);
    }

    @Test
    public void create() {
        TestEntity entity = readEntity(ENTITY);
        entity.setId(null);
        TestEntity saved = readEntity(SAVED);
        when(repository.save(entity)).thenReturn(saved);

        TestEntity result = crudService.create(entity);

        assertThat(result).isSameAs(saved);
    }

    @Test(expected = CreateException.class)
    public void create_WithId() {
        TestEntity entity = readEntity();
        crudService.create(entity);
    }

    @Test
    public void update() {
        TestEntity entity = readEntity(ENTITY);
        TestEntity saved = readEntity(SAVED);
        when(repository.save(entity)).thenReturn(saved);

        TestEntity result = crudService.update(entity);

        assertThat(result).isSameAs(saved);
    }

    @Test(expected = UpdateException.class)
    public void update_WithoutId() {
        TestEntity entity = readEntity();
        entity.setId(null);

        crudService.update(entity);
    }

    @Test
    public void patch() {
        TestEntity patch = readEntity("patch");
        TestEntity entity = readEntity(ENTITY);
        TestEntity saved = readEntity(SAVED);
        when(repository.findById(2L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(saved);

        TestEntity result = crudService.patch(patch);

        verify(patchService).patch(patch, entity);
        verify(validator).validate(entity, Update.class);
        assertThat(result).isSameAs(saved);
    }

    @Test(expected = PatchException.class)
    public void patch_WithoutId() {
        TestEntity entity = readEntity();
        entity.setId(null);
        crudService.patch(entity);
    }

    @Test
    public void delete() {
        TestEntity entity = readEntity();

        crudService.delete(entity);

        verify(repository).delete(captor.capture());
        assertThat(captor.getValue()).isSameAs(entity);
    }

    private TestEntity readEntity() {
        return JsonUtils.fromJson("/json/test-entity.json", TestEntity.class);
    }

    private TestEntity readEntity(String myField) {
        TestEntity entity = readEntity();
        entity.setMyField(myField);
        return entity;
    }
}