package dao;

import model.Horse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class HorseDAOImpl implements HorseDAO {

    @Override
    public void save(Horse horse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(horse);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving horse: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Horse horse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(horse);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating horse: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Horse horse) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(horse);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting horse: " + e.getMessage(), e);
        }
    }

    @Override
    public Horse findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Horse.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding horse by id: " + e.getMessage(), e);
        }
    }

    @Override
    public Horse findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Horse> query = session.createQuery("FROM Horse WHERE name = :name", Horse.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error finding horse by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Horse> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Horse", Horse.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all horses: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Horse> findByStableId(Long stableId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Horse> query = session.createQuery("FROM Horse h WHERE h.stable.id = :stableId", Horse.class);
            query.setParameter("stableId", stableId);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding horses by stable id: " + e.getMessage(), e);
        }
    }
}
