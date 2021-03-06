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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import software.plusminus.crud.model.Create;
import software.plusminus.crud.model.Delete;
import software.plusminus.crud.model.Patch;
import software.plusminus.crud.model.Update;

@SuppressWarnings("squid:S00119")
public interface CrudApi<T, ID> {

    @GetMapping("{id}")
    T getById(@PathVariable ID id);

    @GetMapping
    Page<T> getPage(@PageableDefault(direction = Sort.Direction.DESC) Pageable pageable);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    T create(@Validated(Create.class) @RequestBody T object);

    @PutMapping
    T update(@Validated(Update.class) @RequestBody T object);

    @PatchMapping
    T patch(@Validated(Patch.class) @RequestBody T patch);

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@Validated(Delete.class) @RequestBody T object);

}
