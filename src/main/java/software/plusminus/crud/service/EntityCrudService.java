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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import software.plusminus.crud.exception.CreateException;
import software.plusminus.crud.exception.NotFoundException;
import software.plusminus.crud.exception.PatchException;
import software.plusminus.crud.exception.UpdateException;
import software.plusminus.crud.model.Update;
import software.plusminus.crud.repository.CrudRepository;
import software.plusminus.patch.service.PatchService;
import software.plusminus.util.EntityUtils;

import javax.validation.Validator;

@SuppressWarnings("squid:S00119")
public abstract class EntityCrudService<T, ID> implements CrudService<T, ID>  {

    private Validator validator;
    private PatchService patchService;
    private CrudRepository<T, ID> repository;

    public EntityCrudService() {
    }

    public EntityCrudService(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    public EntityCrudService(Validator validator, PatchService patchService, CrudRepository<T, ID> repository) {
        this.validator = validator;
        this.patchService = patchService;
        this.repository = repository;
    }

    @Override
    public T getById(ID id) {
        return repository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<T> getPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public T create(T object) {
        if (EntityUtils.findId(object) != null) {
            throw new CreateException("Id is present");
        }
        return repository.save(object);
    }

    @Override
    public T update(T object) {
        if (EntityUtils.findId(object) == null) {
            throw new UpdateException("Id is not present");
        }
        return repository.save(object);
    }

    @Override
    public T patch(T patch) {
        Object id = EntityUtils.findId(patch);
        if (id == null) {
            throw new PatchException("Id is not present");
        }
        T target = getById((ID) id);
        patchService.patch(patch, target);
        validator.validate(target, Update.class);
        return repository.save(target);
    }

    @Override
    public void delete(T object) {
        repository.delete(object);
    }
}
