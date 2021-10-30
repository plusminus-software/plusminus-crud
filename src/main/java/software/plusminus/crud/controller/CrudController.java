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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import software.plusminus.crud.service.CrudService;

@SuppressWarnings("squid:S00119")
public abstract class CrudController<T, ID> implements CrudApi<T, ID> {

    private CrudService<T, ID> service;

    public CrudController() {
    }

    public CrudController(CrudService<T, ID> service) {
        this.service = service;
    }

    @Override
    public T getById(ID id) {
        return service.getById(id);
    }

    @Override
    public Page<T> getPage(Pageable pageable) {
        return service.getPage(pageable);
    }

    @Override
    public T create(T object) {
        return service.create(object);
    }

    @Override
    public T update(T object) {
        return service.update(object);
    }

    @Override
    public T patch(T patch) {
        return service.patch(patch);
    }

    @Override
    public void delete(T object) {
        service.delete(object);
    }
}
