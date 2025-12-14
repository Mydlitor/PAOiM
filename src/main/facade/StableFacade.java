package facade;

import dao.HorseDAO;
import dao.HorseDAOImpl;
import dao.StableDAO;
import dao.StableDAOImpl;
import exceptions.*;
import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

/**
 * Facade pattern to separate UI layer from business logic
 */
public class StableFacade {
    private final StableManager stableManager;
    private final StableDAO stableDAO;
    private final HorseDAO horseDAO;

    public StableFacade() {
        this.stableManager = new StableManager();
        this.stableDAO = new StableDAOImpl();
        this.horseDAO = new HorseDAOImpl();
    }

    // Stable operations
    public void addStable(String name, int capacity) throws StableException {
        stableManager.addStable(name, capacity);
    }

    public void removeStable(String name) throws StableException {
        stableManager.removeStable(name);
    }

    public List<Stable> getAllStables() {
        return stableManager.getAllStables();
    }

    public List<Stable> sortStablesByLoad() {
        return stableManager.sortStablesByLoad();
    }

    public List<Stable> findEmptyStables() {
        return stableManager.findEmpty();
    }

    public Stable getStable(String name) throws StableException {
        return stableManager.getStable(name);
    }

    // Horse operations
    public void addHorseToStable(String stableName, String horseName, String breed, 
                                  HorseType type, HorseCondition condition, 
                                  int age, double price, double weight) throws StableException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Stable stable = stableDAO.findByName(stableName);
            if (stable == null) {
                throw new StableNotFoundException(stableName);
            }
            
            Horse horse = new Horse(horseName, breed, type, condition, age, price, weight);
            stable.addHorse(horse);
            
            session.update(stable);
            transaction.commit();
        } catch (StableException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error adding horse: " + e.getMessage(), e);
        }
    }

    public void removeHorseFromStable(String stableName, String horseName) throws StableException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Stable stable = stableDAO.findByName(stableName);
            if (stable == null) {
                throw new StableNotFoundException(stableName);
            }
            
            Horse horse = stable.search(horseName);
            if (horse == null) {
                throw new HorseNotFoundException(horseName);
            }
            
            stable.removeHorse(horse);
            session.update(stable);
            transaction.commit();
        } catch (StableException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error removing horse: " + e.getMessage(), e);
        }
    }

    public List<Horse> getHorsesInStable(String stableName) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.getHorseList();
    }

    public List<Horse> searchHorsesByName(String stableName, String fragment) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.searchPartial(fragment);
    }

    public List<Horse> filterHorsesByCondition(String stableName, HorseCondition condition) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.getHorseList().stream()
                .filter(h -> h.getCondition() == condition)
                .toList();
    }

    public List<Horse> sortHorsesByName(String stableName) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.sortByName();
    }

    public List<Horse> sortHorsesByPrice(String stableName) throws StableException {
        Stable stable = stableManager.getStable(stableName);
        return stable.sortByPrice();
    }

    public void changeHorseCondition(String stableName, String horseName, HorseCondition condition) throws StableException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Stable stable = stableDAO.findByName(stableName);
            if (stable == null) {
                throw new StableNotFoundException(stableName);
            }
            
            Horse horse = stable.search(horseName);
            if (horse == null) {
                throw new HorseNotFoundException(horseName);
            }
            
            stable.changeCondition(horse, condition);
            session.update(stable);
            transaction.commit();
        } catch (StableException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error changing horse condition: " + e.getMessage(), e);
        }
    }

    public void changeHorseWeight(String stableName, String horseName, double weight) throws StableException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Stable stable = stableDAO.findByName(stableName);
            if (stable == null) {
                throw new StableNotFoundException(stableName);
            }
            
            Horse horse = stable.search(horseName);
            if (horse == null) {
                throw new HorseNotFoundException(horseName);
            }
            
            stable.changeWeight(horse, weight);
            session.update(stable);
            transaction.commit();
        } catch (StableException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error changing horse weight: " + e.getMessage(), e);
        }
    }
}
