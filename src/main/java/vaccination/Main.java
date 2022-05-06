package vaccination;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Citizen johnDoe = new Citizen("John Doe", "3304", LocalDate.of(1985, 5,24), "johndoe.mail.com", "123456788");

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pu");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(johnDoe);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }
}
