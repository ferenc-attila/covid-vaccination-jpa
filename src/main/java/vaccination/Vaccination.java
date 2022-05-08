package vaccination;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vaccinations")
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @Column(name = "vaccination_time")
    LocalDateTime timeOfVaccination;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaccination_type")
    private VaccinationType vaccinationType;

    public Vaccination() {
    }

    public Vaccination(Citizen citizen, LocalDateTime timeOfVaccination) {
        this.citizen = citizen;
        this.status = Status.BOOKED;
        this.timeOfVaccination = timeOfVaccination;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public LocalDateTime getTimeOfVaccination() {
        return timeOfVaccination;
    }

    public void setTimeOfVaccination(LocalDateTime timeOfVaccination) {
        this.timeOfVaccination = timeOfVaccination;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public VaccinationType getVaccinationType() {
        return vaccinationType;
    }

    public void setVaccinationType(VaccinationType vaccinationType) {
        this.vaccinationType = vaccinationType;
    }
}
