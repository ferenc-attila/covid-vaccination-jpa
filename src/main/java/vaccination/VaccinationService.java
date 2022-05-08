package vaccination;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VaccinationService {

    private static final int MIN_DAYS_BETWEEN_VACCINATIONS = 15;
    private static final int MAX_NUMBER_OF_VACCINATIONS_PER_DAY = 16;
    private VaccinationDao vaccinationDao;

    private CitizenDao citizenDao;

    public VaccinationService(VaccinationDao vaccinationDao, CitizenDao citizenDao) {
        this.vaccinationDao = vaccinationDao;
        this.citizenDao = citizenDao;
    }

    public Vaccination saveVaccination(Vaccination vaccination) {
        return vaccinationDao.saveVaccination(vaccination);
    }

    public Vaccination findVaccinationById(long id) {
        return vaccinationDao.findVaccinationById(id);
    }

    public List<String> createVaccinationOrderByPostalCode(String postalCode, LocalDate date) {
        LocalDateTime dateTime = date.atTime(7, 30);
        List<Citizen> citizens = citizenDao.findCitizensByPostalCode(postalCode);
        citizens = filterCitizensCanBeVaccinated(dateTime, citizens);
        saveVaccinations(dateTime, citizens);
        return createVaccinationOrderStrings(date, postalCode);
    }

    private List<String> createVaccinationOrderStrings(LocalDate date, String postalCode) {
        List<Vaccination> vaccinations = vaccinationDao.findVaccinationsByDateAndPostalCode(date, postalCode);
        List<String> result = new ArrayList<>();
        for (Vaccination vaccination : vaccinations) {
            result.add(String.join(";", vaccination.getTimeOfVaccination().toLocalTime().toString(),
                    vaccination.getCitizen().getName(),
                    vaccination.getCitizen().getPostalCode(),
                    Long.toString(vaccination.getCitizen().getAge(date)),
                    vaccination.getCitizen().getEmail(),
                    vaccination.getCitizen().getSocialSecurityNumber()));
        }
        return result;
    }

    public String realizeVaccination(Citizen citizen, LocalDate date, boolean realized, VaccinationType vaccinationType, String note) {
        findCitizenInVaccinationOrder(citizen, date);
        checkLastRealizedVaccinationType(citizen, vaccinationType);
        Vaccination vaccination = findLastRealizedVaccination(citizen);
        if (realized) {
            vaccination = vaccinationDao.realizeVaccination(vaccination, vaccinationType);
            return citizen.getName() + " successfully vaccinated with " + vaccinationType.toString() + " on " + vaccination.timeOfVaccination.toString();
        } else {
            vaccination = vaccinationDao.rejectVaccination(vaccination, note);
            return citizen.getName() + " rejected vaccination on " + vaccination.timeOfVaccination.toString() + " with notes: " + vaccination.getNote();
        }
    }

    private List<Vaccination> getRealizedVaccinations(Citizen citizen) {
        return citizen.getVaccinations().stream()
                .filter(vaccination -> vaccination.getStatus() == Status.SUCCESSFUL)
                .toList();
    }

    private Vaccination findLastRealizedVaccination(Citizen citizen) {
        List<Vaccination> realizedVaccinations = getRealizedVaccinations(citizen);
        int numberOfVaccinations = realizedVaccinations.size();
        return vaccinationDao.findVaccinationById(realizedVaccinations.get(numberOfVaccinations - 1).getId());
    }

    private void checkLastRealizedVaccinationType(Citizen citizen, VaccinationType vaccinationType) {
        List<Vaccination> realizedVaccinations = getRealizedVaccinations(citizen);
        int numberOfVaccinations = realizedVaccinations.size();
        if (numberOfVaccinations > 0 && realizedVaccinations.get(numberOfVaccinations - 1).getVaccinationType() != vaccinationType) {
            throw new IllegalArgumentException(vaccinationType.toString() + " can not be used for vaccinating " + citizen.getName() + "!");
        }
    }

    private void findCitizenInVaccinationOrder(Citizen citizen, LocalDate date) {
        List<Vaccination> vaccinations = vaccinationDao.findVaccinationsByDateAndPostalCode(date, citizen.getPostalCode());
        Optional<Citizen> result = vaccinations.stream()
                .map(Vaccination::getCitizen)
                .filter(c -> c.equals(citizen))
                .findFirst();
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Citizen did not registered to this day!");
        }
    }

    private void saveVaccinations(LocalDateTime dateTime, List<Citizen> citizens) {
        for (Citizen citizen : citizens) {
            Vaccination vaccination = new Vaccination(citizen, dateTime.plusMinutes(30));
            vaccinationDao.saveVaccination(vaccination);
        }
    }

    private List<Citizen> filterCitizensCanBeVaccinated(LocalDateTime actualDateTime, List<Citizen> citizens) {
        return citizens.stream()
                .filter(citizen -> citizen.getVaccinations()
                        .stream()
                        .filter(vaccination -> vaccination.getTimeOfVaccination()
                                .isAfter(actualDateTime.minusDays(MIN_DAYS_BETWEEN_VACCINATIONS)))
                        .toList().isEmpty())
                .limit(MAX_NUMBER_OF_VACCINATIONS_PER_DAY)
                .toList();
    }
}
