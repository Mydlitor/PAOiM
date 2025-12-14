package dao;

import javax.persistence.criteria.*;
import model.Horse;
import model.Rating;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingDAOImpl implements RatingDAO {

    @Override
    public void save(Rating rating) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(rating);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving rating: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Rating rating) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(rating);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating rating: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Rating rating) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(rating);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting rating: " + e.getMessage(), e);
        }
    }

    @Override
    public Rating findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Rating.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding rating by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Rating> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Rating", Rating.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all ratings: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Rating> findByHorseId(Long horseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Rating> query = session.createQuery("FROM Rating r WHERE r.horse.id = :horseId", Rating.class);
            query.setParameter("horseId", horseId);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding ratings by horse id: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getAverageRatingByHorse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
            Root<Rating> root = criteriaQuery.from(Rating.class);
            Join<Rating, Horse> horseJoin = root.join("horse");

            criteriaQuery.multiselect(
                    horseJoin.get("name"),
                    criteriaBuilder.avg(root.get("ratingValue")),
                    criteriaBuilder.count(root)
            );
            criteriaQuery.groupBy(horseJoin.get("name"));

            List<Object[]> results = session.createQuery(criteriaQuery).getResultList();
            
            Map<String, Object> resultMap = new HashMap<>();
            for (Object[] result : results) {
                String horseName = (String) result[0];
                Double avgRating = (Double) result[1];
                Long count = (Long) result[2];
                Map<String, Object> stats = new HashMap<>();
                stats.put("average", avgRating);
                stats.put("count", count);
                resultMap.put(horseName, stats);
            }
            
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException("Error getting average rating by horse: " + e.getMessage(), e);
        }
    }
}
