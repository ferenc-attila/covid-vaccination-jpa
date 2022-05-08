package vaccination;

import java.time.LocalDate;
import java.util.Map;

public class CitizenService {

    private CitizenDao citizenDao;

    public CitizenService(CitizenDao citizenDao) {
        this.citizenDao = citizenDao;
    }

    public Citizen saveCitizen(Map<String, String> dataOfCitizen, LocalDate actualDate) {
        Citizen citizen = new Citizen();
        citizen.setDataByMap(dataOfCitizen, actualDate);
        return citizenDao.saveCitizen(citizen);
    }
}
