package ppryvarnikov.ps1.dao;

import java.util.Optional;
import java.util.Set;

public interface Dao<ENTITY, ID_TYPE> {
    Optional<ENTITY> get(ID_TYPE id);
    Set<ENTITY> getAll();
    boolean insert(ENTITY entity);
    boolean update(ENTITY entity);
    boolean delete(ID_TYPE id);
}
