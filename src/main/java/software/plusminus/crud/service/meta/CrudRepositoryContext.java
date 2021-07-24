package software.plusminus.crud.service.meta;

import software.plusminus.crud.repository.CrudRepository;

@SuppressWarnings("squid:S00119")
public interface CrudRepositoryContext {
    
    <T, ID> CrudRepository<T, ID> findCrudRepository(Class<T> type);

    <ID> Class<ID> getIdType(String type);

}
