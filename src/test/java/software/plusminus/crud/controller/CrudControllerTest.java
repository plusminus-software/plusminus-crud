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
package software.plusminus.crud.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import software.plusminus.check.util.JsonUtils;
import software.plusminus.crud.TestEntity;
import software.plusminus.crud.service.CrudService;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CrudControllerTest {

    @Mock
    private CrudService<TestEntity, Long> service;
    @InjectMocks
    private CrudController<TestEntity, Long> controller = mock(CrudController.class, CALLS_REAL_METHODS);
    
    @Test
    public void getById() {
        controller.getById(42L);
        verify(service).getById(42L);
    }

    @Test
    public void getPage() {
        Pageable pageable = PageRequest.of(2, 20);
        controller.getPage(pageable);
        verify(service).getPage(pageable);
    }

    @Test
    public void create() {
        TestEntity testEntity = readEntity();
        controller.create(testEntity);
        verify(service).create(testEntity);
    }

    @Test
    public void update() {
        TestEntity testEntity = readEntity();
        controller.update(testEntity);
        verify(service).update(testEntity);
    }

    @Test
    public void patch() {
        TestEntity testEntity = readEntity();
        controller.patch(testEntity);
        verify(service).patch(testEntity);
    }

    @Test
    public void delete() {
        TestEntity testEntity = readEntity();
        controller.delete(testEntity);
        verify(service).delete(testEntity);
    }
    
    private TestEntity readEntity() {
        return JsonUtils.fromJson("/json/test-entity.json", TestEntity.class);
    }
}