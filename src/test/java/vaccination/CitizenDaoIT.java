package vaccination;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CitizenDaoIT {

    private EntityManagerFactory factory;

    private CitizenDao citizenDao;

    private VaccinationDao vaccinationDao;

    @BeforeEach
    void setUp() {
        factory = Persistence.createEntityManagerFactory("pu");
        citizenDao = new CitizenDao(factory);
        vaccinationDao = new VaccinationDao(factory);
    }

    @AfterEach
    void tearDown() {
        factory.close();
    }

    @Test
    void saveAndFindCitizenTest() {
        Citizen johnDoe = new Citizen(
                "John Doe",
                "3304",
                "1985-05-24",
                "johndoe@mail.com",
                "123456788",
                LocalDate.of(1995,5,24));
        citizenDao.saveCitizen(johnDoe);

        johnDoe = citizenDao.findCitizenById(johnDoe.getId());

        assertThat(johnDoe.getEmail()).isEqualTo("johndoe@mail.com");
    }

    @Test
    void findCitizenWithVaccinationsTest() {
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
        Vaccination secondVaccination = new Vaccination(johnDoe,
                LocalDateTime.of(2022,6,24,8,0));
        vaccinationDao.saveVaccination(secondVaccination);

        johnDoe = citizenDao.findCitizenWithVaccinations(johnDoe.getId());

        assertThat(johnDoe.getVaccinations())
                .extracting(Vaccination::getTimeOfVaccination)
                .containsExactly(LocalDateTime.of(2022,5,24,8,0), LocalDateTime.of(2022,6,24,8,0));
    }

    @Test
    void findCitizenWithPostalCodeTest() {
        Citizen johnDoe = new Citizen(
                "John Doe",
                "3304",
                "1985-05-24",
                "johndoe@mail.com",
                "123456788",
                LocalDate.of(2022,5,10));
        citizenDao.saveCitizen(johnDoe);
        Citizen janeDoe = new Citizen(
                "Jane Doe",
                "3305",
                "2000-12-24",
                "janedoe@mail.com",
                "123456788",
                LocalDate.of(2022,5,10));
        citizenDao.saveCitizen(janeDoe);
        Citizen jackDoe = new Citizen(
                "Jack Doe",
                "3304",
                "1965-01-31",
                "jackdoe@mail.com",
                "123456788",
                LocalDate.of(2022,5,10));
        citizenDao.saveCitizen(jackDoe);

        List<Citizen> result = citizenDao.findCitizensByPostalCode("3304");

        assertThat(result).extracting(Citizen::getName)
                .containsExactly("Jack Doe", "John Doe");
    }

//    @Test
//    void countNumberOfCitizensByNumberOfVaccinationsAndPostalCodeTest() {
//        Citizen johnDoe = new Citizen(
//                "John Doe",
//                "3304",
//                "1985-05-24",
//                "johndoe@mail.com",
//                "123456788",
//                LocalDate.of(2022,5,10));
//        citizenDao.saveCitizen(johnDoe);
//        Citizen janeDoe = new Citizen(
//                "Jane Doe",
//                "3305",
//                "2000-12-24",
//                "janedoe@mail.com",
//                "123456788",
//                LocalDate.of(2022,5,10));
//        citizenDao.saveCitizen(janeDoe);
//        Citizen jackDoe = new Citizen(
//                "Jack Doe",
//                "3304",
//                "1965-01-31",
//                "jackdoe@mail.com",
//                "123456788",
//                LocalDate.of(2022,5,10));
//        citizenDao.saveCitizen(jackDoe);
//        Citizen jillDoe = new Citizen(
//                "Jill Doe",
//                "3305",
//                "1948-07-11",
//                "jilldoe@mail.com",
//                "123456788",
//                LocalDate.of(2022,5,10));
//        citizenDao.saveCitizen(jillDoe);
//
//        Vaccination firstVaccinationOfJohnDoe = new Vaccination(johnDoe,
//                LocalDateTime.of(2022,3,24,8,0));
//        vaccinationDao.saveVaccination(firstVaccinationOfJohnDoe);
//        Vaccination secondVaccinationOfJohnDoe = new Vaccination(johnDoe,
//                LocalDateTime.of(2022,4,24,8,0));
//        vaccinationDao.saveVaccination(secondVaccinationOfJohnDoe);
//        Vaccination thirdVaccinationOfJohnDoe = new Vaccination(johnDoe,
//                LocalDateTime.of(2022,5,24,8,0));
//        vaccinationDao.saveVaccination(thirdVaccinationOfJohnDoe);
//        Vaccination firstVaccinationOfJaneDoe = new Vaccination(janeDoe,
//                LocalDateTime.of(2022,5,24,8,0));
//        vaccinationDao.saveVaccination(firstVaccinationOfJaneDoe);
//        Vaccination secondVaccinationOfJaneDoe = new Vaccination(janeDoe,
//                LocalDateTime.of(2022,6,24,8,0));
//        vaccinationDao.saveVaccination(secondVaccinationOfJaneDoe);
//        Vaccination firstVaccinationOfJillDoe = new Vaccination(jillDoe,
//                LocalDateTime.of(2022,5,24,8,0));
//        vaccinationDao.saveVaccination(firstVaccinationOfJillDoe);
//        Vaccination secondVaccinationOfJillDoe = new Vaccination(jillDoe,
//                LocalDateTime.of(2022,6,24,8,0));
//        vaccinationDao.saveVaccination(secondVaccinationOfJillDoe);
//        Vaccination firstVaccinationOfJackDoe = new Vaccination(jackDoe,
//                LocalDateTime.of(2022,5,24,8,0));
//        vaccinationDao.saveVaccination(firstVaccinationOfJackDoe);
//
//        List<ReportDTO> result = citizenDao.countNumberOfCitizensByNumberOfVaccinationsAndPostalCode();
//
//        System.out.println(result);
//    }
}