package vaccination;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "citizens")
public class Citizen {

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

    public Citizen() {
    }

    public Citizen(String name, String postalCode, LocalDate dateOfBirth, String email, String socialSecurityNumber) {
        this.name = name;
        this.postalCode = postalCode;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public Citizen(Long id, String name, String postalCode, LocalDate dateOfBirth, String email, String socialSecurityNumber) {
        this.id = id;
        this.name = name;
        this.postalCode = postalCode;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.socialSecurityNumber = socialSecurityNumber;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }
}
