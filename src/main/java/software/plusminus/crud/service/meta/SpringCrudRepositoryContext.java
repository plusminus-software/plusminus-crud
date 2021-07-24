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
package software.plusminus.crud.service.meta;

import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import software.plusminus.crud.exception.NotFoundException;
import software.plusminus.crud.repository.CrudRepository;

import java.util.stream.StreamSupport;

@SuppressWarnings("squid:S00119")
@Service
public class SpringCrudRepositoryContext implements CrudRepositoryContext {

    private Repositories repositories;

    public SpringCrudRepositoryContext(WebApplicationContext appContext) {
        repositories = new Repositories(appContext);
    }

    @Override
    public <T, ID> CrudRepository<T, ID> findCrudRepository(Class<T> type) {
        return (CrudRepository<T, ID>) repositories.getRepositoryFor(type)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public <ID> Class<ID> getIdType(String type) {
        return repositories.getRepositoryInformationFor(getType(type))
                .map(RepositoryInformation::getIdType)
                .map(c -> (Class<ID>) c)
                .orElseThrow(NotFoundException::new);
    }

    private <T> Class<T> getType(String type) {
        return StreamSupport.stream(repositories.spliterator(), false)
                .filter(c -> c.getSimpleName().equals(type))
                .findFirst()
                .map(c -> (Class<T>) c)
                .orElseThrow(NotFoundException::new);
    }
}