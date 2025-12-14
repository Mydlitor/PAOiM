package util;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class TestDatabaseUtil {
    
    public static void clearDatabase() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Delete all ratings first (has foreign keys)
            session.createQuery("DELETE FROM Rating").executeUpdate();
            // Delete all horses
            session.createQuery("DELETE FROM Horse").executeUpdate();
            // Delete all stables
            session.createQuery("DELETE FROM Stable").executeUpdate();
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error clearing database: " + e.getMessage());
        }
    }
}
