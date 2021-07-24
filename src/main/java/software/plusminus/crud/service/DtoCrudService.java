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

import company.plusminus.patch.service.PatchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import software.plusminus.crud.exception.CreateException;
import software.plusminus.crud.exception.NotFoundException;
import software.plusminus.crud.exception.PatchException;
import software.plusminus.crud.exception.UpdateException;
import software.plusminus.crud.model.Update;
import software.plusminus.crud.repository.CrudRepository;
import software.plusminus.populate.Populate;
import software.plusminus.util.EntityUtils;

import javax.validation.Validator;

@SuppressWarnings("squid:S00119")
public abstract class DtoCrudService<DTO, E, ID> implements CrudService<DTO, ID>  {

    @Populate
    private PatchService patchService;
    @Populate
    private DtoConverter<DTO, E> converter;
    @Populate
    private Validator validator;
    @Populate
    private CrudRepository<E, ID> repository;

    public DtoCrudService() {
    }

    public DtoCrudService(DtoConverter<DTO, E> converter,
                          CrudRepository<E, ID> repository) {
        this.converter = converter;
        this.repository = repository;
    }

    public DtoCrudService(PatchService patchService,
                          DtoConverter<DTO, E> converter,
                          Validator validator,
                          CrudRepository<E, ID> repository) {
        this.patchService = patchService;
        this.converter = converter;
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public DTO getById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        return converter.toDto(entity);
    }

    @Override
    public Page<DTO> getPage(Pageable pageable) {
        return repository.findAll(pageable)
                .map(element -> converter.toDto(element));
    }

    @Override
    public DTO create(DTO dto) {
        E converted = converter.toEntity(dto);
        if (EntityUtils.findId(converted) != null) {
            throw new CreateException("Id is present");
        }
        E result = repository.save(converted);
        return converter.toDto(result);
    }

    @Override
    public DTO update(DTO dto) {
        E converted = converter.toEntity(dto);
        if (EntityUtils.findId(converted) == null) {
            throw new UpdateException("Id is not present");
        }
        E result = repository.save(converted);
        return converter.toDto(result);
    }

    @Override
    public DTO patch(DTO patch) {
        E convertedPatch = converter.toEntity(patch);
        Object id = EntityUtils.findId(convertedPatch);
        if (id == null) {
            throw new PatchException("Id is not present");
        }
        DTO target = getById((ID) id);
        patchService.patch(patch, target);
        validator.validate(target, Update.class);
        return update(target);
    }

    @Override
    public void delete(DTO dto) {
        E converted = converter.toEntity(dto);
        repository.delete(converted);
    }
}
