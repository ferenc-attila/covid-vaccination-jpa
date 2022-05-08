package vaccination;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "citizens")
public class Citizen {

    @Transient
    private static final int MINIMUM_AGE = 10;

    @Transient
    private static final int MAXIMUM_AGE = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_name", nullable = false)
    private String name;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    private String email;

    @Column(name = "ssn", nullable = false)
    private String socialSecurityNumber;

    @OneToMany(mappedBy = "citizen",
    orphanRemoval = true)
    @OrderBy(value = "timeOfVaccination")
    private List<Vaccination> vaccinations = new ArrayList<>();

    public Citizen() {
    }

    public Citizen(String name, String postalCode, String dateOfBirth, String email, String socialSecurityNumber, LocalDate actualDate) {
        this.name = checkIfStringIsEmpty(name);
        this.postalCode = checkIfStringIsEmpty(postalCode);
        this.dateOfBirth = validateDateOfBirthString(dateOfBirth, actualDate);
        this.email = validateEmail(email);
        this.socialSecurityNumber = validateSocialSecurityNumber(socialSecurityNumber);
    }

    public Citizen(Long id, String name, String postalCode, LocalDate dateOfBirth, String email, String socialSecurityNumber) {
        this.id = id;
        this.name = name;
        this.postalCode = postalCode;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public void addVaccination (Vaccination vaccination) {
        vaccinations.add(vaccination);
        vaccination.setCitizen(this);
    }

    public void setDataByMap(Map<String, String> dataOfCitizen, LocalDate actualDate) {
        this.name = checkIfStringIsEmpty(dataOfCitizen.get("name"));
        this.postalCode = checkIfStringIsEmpty(dataOfCitizen.get("postalCode"));
        this.dateOfBirth = validateDateOfBirthString(dataOfCitizen.get("dateOfBirth"), actualDate);
        this.email = validateEmail(dataOfCitizen.get("email"));
        this.socialSecurityNumber = validateSocialSecurityNumber(dataOfCitizen.get("ssn"));
    }

    public long getAge(LocalDate date) {
        return ChronoUnit.YEARS.between(date, dateOfBirth);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public List<Vaccination> getVaccinations() {
        return vaccinations;
    }

    private String checkIfStringIsEmpty(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Value cannot be null or empty!");
        }
        return text.strip();
    }

    private LocalDate validateDateOfBirthString(String dateOfBirthString, LocalDate actualDate) {
        checkIfStringIsEmpty(dateOfBirthString);
        LocalDate birthday;
        try {
            birthday = LocalDate.parse(dateOfBirthString);
        } catch (DateTimeParseException dtpe) {
            throw new IllegalArgumentException("Date format is invalid: " + dateOfBirthString, dtpe);
        }
        return validateAgeOfCitizen(birthday, actualDate);
    }

    private LocalDate validateAgeOfCitizen(LocalDate dateOfBirth, LocalDate actualDate) {
        if (dateOfBirth.isAfter(actualDate.minusYears(MINIMUM_AGE)) || dateOfBirth.isBefore(actualDate.minusYears(MAXIMUM_AGE))) {
            throw new IllegalArgumentException(dateOfBirth + " is invalid date of birth. Citizen must be older than " + MINIMUM_AGE + " years, and must be younger than " + MAXIMUM_AGE + " years!");
        }
        return dateOfBirth;
    }

    private String validateEmail(String email) {
        checkIfStringIsEmpty(email);
        if (email.length() < 5) {
            throw new IllegalArgumentException("Email: '" + email + "' is too short.");
        }
        if (email.indexOf('@') < 1) {
            throw new IllegalArgumentException(
                    "Invalid email: '" + email + "'. It must contain @. It must be at least in the second character of the string!");
        }
        if (email.lastIndexOf('.') <= email.indexOf('@') + 1) {
            throw new IllegalArgumentException(
                    "Invalid email: '" + email + "'. It must contain a dot. The last dot must be after a substring following @!");
        }
        return email;
    }

    private String validateSocialSecurityNumber(String socialSecurityNumber) {
        checkIfStringIsEmpty(socialSecurityNumber);
        if (socialSecurityNumber.length() != 9) {
            throw new IllegalArgumentException("Social security number must be exactly 9 characters long!");
        }
        if (!cdvValidation(socialSecurityNumber)) {
            throw new IllegalArgumentException(socialSecurityNumber + "'s CDV code check failed!");
        }
        return socialSecurityNumber;
    }

    private boolean cdvValidation(String socialSecurityNumber) {
        char[] charsOfSsn = socialSecurityNumber.toCharArray();
        int cdvCode = 0;
        for (int i = 0; i < charsOfSsn.length - 1; i++) {
            if (i % 2 == 0) {
               cdvCode += Character.getNumericValue(charsOfSsn[i]) * 3;
            } else {
                cdvCode += Character.getNumericValue(charsOfSsn[i]) * 7;
            }
        }
        return cdvCode % 10 == Character.getNumericValue(charsOfSsn[8]);
    }
}
