package dao;

import model.Stable;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class StableDAOImpl implements StableDAO {

    @Override
    public void save(Stable stable) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(stable);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving stable: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Stable stable) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(stable);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating stable: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Stable stable) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(stable);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting stable: " + e.getMessage(), e);
        }
    }

    @Override
    public Stable findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Stable.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding stable by id: " + e.getMessage(), e);
        }
    }

    @Override
    public Stable findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Stable> query = session.createQuery("FROM Stable WHERE stableName = :name", Stable.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error finding stable by name: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Stable> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Stable", Stable.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all stables: " + e.getMessage(), e);
        }
    }
}
