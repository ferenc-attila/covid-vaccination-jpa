package vaccination;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VaccinationDao {

    private EntityManagerFactory factory;

    public VaccinationDao(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public Vaccination saveVaccination(Vaccination vaccination) {
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(vaccination);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
        return vaccination;
    }

    public Vaccination findVaccinationById(long id) {
        EntityManager entityManager = factory.createEntityManager();
        Vaccination result;
        try {
            result = entityManager.find(Vaccination.class, id);
        } finally {
            entityManager.close();
        }
        return result;
    }

    public List<Vaccination> findVaccinationsByDateAndPostalCode(LocalDate date, String postalCode) {
        EntityManager entityManager = factory.createEntityManager();
        List<Vaccination> result = new ArrayList<>();
        try {
            result.addAll(entityManager.createQuery(
                            "select v from Vaccination v where v.timeOfVaccination between :startDate and :endDate and v.citizen.postalCode = :postalCode",
                            Vaccination.class)
                    .setParameter("startDate", date)
                    .setParameter("endDate", date.plusDays(1))
                    .setParameter("postalCode", postalCode)
                    .getResultList());
        } finally {
            entityManager.close();
        }
        return result;
    }

    public Vaccination realizeVaccination(Vaccination vaccination, VaccinationType vaccinationType) {
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            vaccination.setVaccinationType(vaccinationType);
            vaccination.setStatus(Status.SUCCESSFUL);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
        return vaccination;
    }

    public Vaccination rejectVaccination(Vaccination vaccination, String note) {
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            vaccination.setStatus(Status.REJECTED);
            vaccination.setNote(note);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
        return vaccination;
    }
}
