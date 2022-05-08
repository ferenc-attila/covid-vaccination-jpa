package vaccination;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class CitizenDao {

    private EntityManagerFactory factory;

    public CitizenDao(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public Citizen saveCitizen(Citizen citizen) {
        EntityManager entityManager = factory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(citizen);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
        return citizen;
    }

    public Citizen findCitizenById(long id) {
        Citizen result;
        EntityManager entityManager = factory.createEntityManager();
        try {
            result = entityManager.find(Citizen.class, id);
        } finally {
            entityManager.close();
        }
        return result;
    }

    public Citizen findCitizenWithVaccinations(long id) {
        Citizen result;
        EntityManager entityManager = factory.createEntityManager();
        try {
            result = entityManager.createQuery("select c from Citizen c join fetch c.vaccinations where c.id = :id",
                            Citizen.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
        return result;
    }

    public List<Citizen> findCitizensByPostalCode(String postalCode) {
        EntityManager entityManager = factory.createEntityManager();
        List<Citizen> result = new ArrayList<>();
        try {
            result.addAll(entityManager.createQuery(
                    "select distinct c from Citizen c left join fetch Vaccination where c.postalCode = :postalCode order by c.dateOfBirth",
                    Citizen.class)
                    .setParameter("postalCode", postalCode)
                    .getResultList());
        } finally {
            entityManager.close();
        }
        return result;
    }

//    public List<ReportDTO> countNumberOfCitizensByNumberOfVaccinationsAndPostalCode() {
//        EntityManager entityManager = factory.createEntityManager();
//        List<ReportDTO> report;
//        try {
//            report = entityManager.createQuery(
//                    "select new vaccination.ReportDTO(c.postalCode, c.vaccinations.size, count(distinct c.id)) from Citizen c inner join fetch c.vaccinations v group by c.postalCode, c.vaccinations.size order by c.postalCode",
//                    ReportDTO.class)
//                    .getResultList();
//        } finally {
//            entityManager.close();
//        }
//        return report;
//    }
}
