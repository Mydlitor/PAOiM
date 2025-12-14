package dao;

import model.Horse;
import java.util.List;

public interface HorseDAO {
    void save(Horse horse);
    void update(Horse horse);
    void delete(Horse horse);
    Horse findById(Long id);
    Horse findByName(String name);
    List<Horse> findAll();
    List<Horse> findByStableId(Long stableId);
}
