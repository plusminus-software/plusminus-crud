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
import software.plusminus.crud.TestDto;
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
public class DtoCrudServiceTest {

    private static final String ENTITY = "entity";
    private static final String SAVED = "saved";
    private static final String CONVERTED = "converted";

    @Mock
    private Validator validator;
    @Mock
    private PatchService patchService;
    @Mock
    private DtoConverter<TestDto, TestEntity> converter;
    @Mock
    private CrudRepository<TestEntity, Long> repository;
    @InjectMocks
    private DtoCrudService<TestDto, TestEntity, Long> crudService =
            mock(DtoCrudService.class, Answers.CALLS_REAL_METHODS);
    @Captor
    private ArgumentCaptor<TestEntity> captor;

    @Test
    public void readById() {
        TestEntity entity = readEntity();
        TestDto dto = readDto();
        when(repository.findById(2L)).thenReturn(Optional.of(entity));
        when(converter.toDto(entity)).thenReturn(dto);

        TestDto result = crudService.getById(2L);

        assertThat(result).isSameAs(dto);
    }

    @Test(expected = NotFoundException.class)
    public void readById_NotFoundException() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        crudService.getById(2L);
    }

    @Test
    public void readPage() {
        TestEntity entity = readEntity();
        TestDto dto = readDto();
        Pageable pageable = PageRequest.of(2, 3);
        Page<TestEntity> entityPage = new PageImpl<>(Collections.singletonList(entity), pageable, 100);
        Page<TestDto> dtoPage = new PageImpl<>(Collections.singletonList(dto), pageable, 100);
        when(repository.findAll(pageable)).thenReturn(entityPage);
        when(converter.toDto(entity)).thenReturn(dto);

        Page<TestDto> result = crudService.getPage(pageable);

        check(result).is(dtoPage);
    }

    @Test
    public void create() {
        TestEntity entity = readEntity(ENTITY);
        entity.setId(null);
        TestEntity saved = readEntity(SAVED);
        TestDto dto = readDto("dto");
        TestDto converted = readDto(CONVERTED);
        when(converter.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(converter.toDto(saved)).thenReturn(converted);

        TestDto result = crudService.create(dto);

        assertThat(result).isSameAs(converted);
    }

    @Test(expected = CreateException.class)
    public void create_WithId() {
        TestEntity entity = readEntity();
        TestDto dto = readDto();
        when(converter.toEntity(dto)).thenReturn(entity);

        crudService.create(dto);
    }

    @Test
    public void update() {
        TestEntity entity = readEntity(ENTITY);
        TestEntity saved = readEntity(SAVED);
        TestDto dto = readDto("dto");
        TestDto converted = readDto(CONVERTED);
        when(converter.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(converter.toDto(saved)).thenReturn(converted);

        TestDto result = crudService.update(dto);

        assertThat(result).isSameAs(converted);
    }

    @Test(expected = UpdateException.class)
    public void update_WithoutId() {
        TestEntity entity = readEntity();
        entity.setId(null);
        TestDto dto = readDto();
        when(converter.toEntity(dto)).thenReturn(entity);

        crudService.update(dto);
    }

    @Test
    public void patch() {
        TestEntity entity = readEntity(ENTITY);
        TestEntity saved = readEntity(SAVED);
        TestEntity convertedPatch = readEntity("convertedPatch");
        TestDto patch = readDto("patch");
        TestDto converted = readDto(CONVERTED);
        when(converter.toEntity(patch)).thenReturn(convertedPatch);
        when(repository.findById(2L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(saved);
        when(converter.toDto(entity)).thenReturn(converted);
        when(converter.toEntity(converted)).thenReturn(entity);
        when(converter.toDto(saved)).thenReturn(converted);

        TestDto result = crudService.patch(patch);

        verify(patchService).patch(patch, converted);
        verify(validator).validate(converted, Update.class);
        assertThat(result).isSameAs(converted);
    }

    @Test(expected = PatchException.class)
    public void patch_WithoutId() {
        TestEntity entity = readEntity();
        entity.setId(null);
        TestDto dto = readDto();
        when(converter.toEntity(dto)).thenReturn(entity);

        crudService.patch(dto);
    }

    @Test
    public void delete() {
        TestEntity entity = readEntity();
        TestDto dto = readDto();
        when(converter.toEntity(dto)).thenReturn(entity);

        crudService.delete(dto);

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

    private TestDto readDto() {
        return JsonUtils.fromJson("/json/test-dto.json", TestDto.class);
    }

    private TestDto readDto(String myField) {
        TestDto dto = readDto();
        dto.setMyField(myField);
        return dto;
    }
}