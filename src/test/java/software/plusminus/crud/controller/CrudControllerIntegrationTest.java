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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import software.plusminus.check.util.JsonUtils;
import software.plusminus.crud.TestController;
import software.plusminus.crud.TestEntity;
import software.plusminus.crud.service.CrudService;
import software.plusminus.populate.PopulateAnnotationBeanPostProcessor;
import software.plusminus.util.ResourceUtils;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static software.plusminus.check.Checks.check;

@RunWith(SpringRunner.class)
@WebMvcTest({ TestController.class, PopulateAnnotationBeanPostProcessor.class })
@ActiveProfiles("test")
public class CrudControllerIntegrationTest {

    private static final String PATH = "/test";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CrudService<TestEntity, Long> service;
    @Captor
    private ArgumentCaptor<TestEntity> captor;

    @Test
    public void getByIdReturnsCorrectResponse() throws Exception {
        String json = readEntity();
        TestEntity entity = JsonUtils.fromJson(json, TestEntity.class);
        when(service.getById(2L)).thenReturn(entity);

        String body = mvc.perform(get("/test/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        check(body).is(json);
    }

    @Test
    public void getWithPagingReturnsPage() throws Exception {
        String json = readEntity();
        Pageable pageable = PageRequest.of(2, 5);
        TestEntity entity = JsonUtils.fromJson(json, TestEntity.class);
        when(service.getPage(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(entity), pageable, 100));

        String body = mvc.perform(get("/test?page=2&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        check(body).isJson().isIgnoringFieldsOrder("/json/test-entity-page.json");
    }

    @Test
    public void postReturnsCreatedEntity() throws Exception {
        String json = readEntity();
        json = json.replaceAll("\"id\": 2,", "");
        TestEntity entity = JsonUtils.fromJson(json, TestEntity.class);
        when(service.create(entity)).thenReturn(entity);

        String body = mvc.perform(post(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        check(body).is(json);
        verify(service).create(captor.capture());
        check(captor.getValue()).is(json);
    }

    @Test
    public void putReturnsUpdatedEntity() throws Exception {
        String json = readEntity();
        TestEntity entity = JsonUtils.fromJson(json, TestEntity.class);
        when(service.update(entity)).thenReturn(entity);

        String body = mvc.perform(put(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        check(body).is(json);
        verify(service).update(captor.capture());
        check(captor.getValue()).is(json);
    }

    @Test
    public void patchReturnsPatchedEntity() throws Exception {
        String json = readEntity();
        TestEntity entity = JsonUtils.fromJson(json, TestEntity.class);
        when(service.patch(entity)).thenReturn(entity);

        String body = mvc.perform(patch(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        check(body).is(json);
        verify(service).patch(captor.capture());
        check(captor.getValue()).is(json);
    }

    @Test
    public void deleteRemovesEntity() throws Exception {
        String json = readEntity();

        mvc.perform(delete(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service).delete(captor.capture());
        check(captor.getValue()).is(json);
    }
    
    private String readEntity() {
        return ResourceUtils.toString("/json/test-entity.json");
    }
}