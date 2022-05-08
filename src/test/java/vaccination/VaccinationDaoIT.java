package vaccination;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class VaccinationDaoIT {

    EntityManagerFactory factory;

    VaccinationDao vaccinationDao;

    CitizenDao citizenDao;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("pu");
        vaccinationDao = new VaccinationDao(factory);
        citizenDao = new CitizenDao(factory);
    }

    @AfterEach
    void tearDown() {
        factory.close();
    }

    @Test
    void saveAndFindVaccinationTest() {
        Citizen johnDoe = new Citizen(
                "John Doe",
                "3304",
                "1985-05-24",
                "johndoe@mail.com",
                "123456788",
                LocalDate.of(1995,5,24));
        citizenDao.saveCitizen(johnDoe);
        Vaccination vaccination = new Vaccination(johnDoe,
                LocalDateTime.of(2022,5,24,8,0));
        vaccinationDao.saveVaccination(vaccination);

        vaccination = vaccinationDao.findVaccinationById(vaccination.getId());

        assertThat(vaccination.getCitizen().getDateOfBirth()).isEqualTo("1985-05-24");
    }
}