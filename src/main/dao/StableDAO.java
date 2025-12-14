package dao;

import model.Stable;
import java.util.List;

public interface StableDAO {
    void save(Stable stable);
    void update(Stable stable);
    void delete(Stable stable);
    Stable findById(Long id);
    Stable findByName(String name);
    List<Stable> findAll();
}
