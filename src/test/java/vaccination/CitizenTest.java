package vaccination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CitizenTest {

    @Test
    void addVaccinationTest() {
        Citizen citizen = new Citizen();
        Vaccination vaccination = new Vaccination();

        citizen.addVaccination(vaccination);

        assertThat(citizen.getVaccinations()).hasSize(1);
        assertThat(vaccination.getCitizen()).isSameAs(citizen);
    }

    @Test
    void setDataByMapTest() {
        Citizen citizen = new Citizen();
        Map<String, String> dataOfCitizen = new HashMap<>();
        dataOfCitizen.put("name", "John Doe");
        dataOfCitizen.put("postalCode", "3778");
        dataOfCitizen.put("dateOfBirth", "2002-05-25");
        dataOfCitizen.put("email", "johndoe@mail.com");
        dataOfCitizen.put("ssn", "123456788");

        citizen.setDataByMap(dataOfCitizen, LocalDate.of(2022,5,25));

        assertThat(citizen.getDateOfBirth()).isEqualTo("2002-05-25");
    }

    @ParameterizedTest(name = "{6}")
    @CsvFileSource(resources = "/invalid_citizen_data.csv", delimiter = ';', numLinesToSkip = 1)
    void invalidCitizenDataTest(String name, String postalCode, String dateOfBirth, String email, String ssn, String errorMessage, String nameOfTest) {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Citizen(name, postalCode, dateOfBirth, email, ssn, LocalDate.of(2022,5,14)));

        assertThat(iae.getMessage()).isEqualTo(errorMessage);
    }
}