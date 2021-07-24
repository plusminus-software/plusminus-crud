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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import software.plusminus.check.util.JsonUtils;
import software.plusminus.crud.TestDto;
import software.plusminus.crud.TestEntity;
import software.plusminus.crud.exception.ConvertException;

import static org.mockito.Mockito.when;
import static software.plusminus.check.Checks.check;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeDtoConverterTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private RuntimeDtoConverter<TestDto, TestEntity> converter;

    @Before
    public void before() {
        when(converter.dtoType()).thenReturn(TestDto.class);
        when(converter.entityType()).thenReturn(TestEntity.class);
    }

    @Test
    public void toDto() {
        TestEntity entity = JsonUtils.fromJson("/json/test-entity.json", TestEntity.class);
        TestDto dto = JsonUtils.fromJson("/json/test-same-dto.json", TestDto.class);

        TestDto result = converter.toDto(entity);

        check(result).is(dto);
    }

    @Test(expected = ConvertException.class)
    public void toDto_Exception() {
        TestEntity entity = JsonUtils.fromJson("/json/test-entity.json", TestEntity.class);
        when(converter.dtoType()).thenAnswer(a -> {
            throw new InstantiationException();
        });

        converter.toDto(entity);
    }

    @Test
    public void toEntity() {
        TestEntity entity = JsonUtils.fromJson("/json/test-entity.json", TestEntity.class);
        TestDto dto = JsonUtils.fromJson("/json/test-same-dto.json", TestDto.class);

        TestEntity result = converter.toEntity(dto);

        check(result).is(entity);
    }

    @Test(expected = ConvertException.class)
    public void toEntity_Exception() {
        TestDto dto = JsonUtils.fromJson("/json/test-same-dto.json", TestDto.class);
        when(converter.entityType()).thenAnswer(a -> {
            throw new InstantiationException();
        });

        converter.toEntity(dto);
    }
}