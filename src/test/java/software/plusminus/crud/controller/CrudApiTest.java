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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import software.plusminus.crud.DummyCrudController;
import software.plusminus.crud.TestEntity;
import software.plusminus.util.ResourceUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static software.plusminus.check.Checks.check;

@RunWith(SpringRunner.class)
@WebMvcTest(DummyCrudController.class)
@ActiveProfiles("test")
public class CrudApiTest {

    private static final String PATH = "/test";

    @Autowired
    private MockMvc mvc;
    @SpyBean
    private DummyCrudController crudController;
    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;
    @Captor
    private ArgumentCaptor<TestEntity> entityCaptor;

    @Test
    public void getById() throws Exception {
        mvc.perform(get("/test/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        verify(crudController).getById(2L);
    }

    @Test
    public void getPage() throws Exception {
        mvc.perform(get("/test?page=2&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        verify(crudController).getPage(pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    public void create() throws Exception {
        String json = readEntity();
        mvc.perform(post(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(crudController).create(entityCaptor.capture());
        TestEntity actual = entityCaptor.getValue();
        check(actual).is(json);
    }

    @Test
    public void update() throws Exception {
        String json = readEntity();
        mvc.perform(put(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(crudController).update(entityCaptor.capture());
        TestEntity actual = entityCaptor.getValue();
        check(actual).is(json);
    }

    @Test
    public void patch() throws Exception {
        String json = readEntity();
        mvc.perform(MockMvcRequestBuilders.patch(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(crudController).patch(entityCaptor.capture());
        TestEntity actual = entityCaptor.getValue();
        check(actual).is(json);
    }

    @Test
    public void delete() throws Exception {
        String json = readEntity();
        mvc.perform(MockMvcRequestBuilders.delete(PATH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(crudController).delete(entityCaptor.capture());
        TestEntity actual = entityCaptor.getValue();
        check(actual).is(json);
    }
    
    private String readEntity() {
        return ResourceUtils.toString("/json/test-entity.json");
    }
}