package vaccination;

public class ReportDTO {

    private String postalCode;

    private int numberOfVaccinations;

    private Long countOfCitizens;

    public ReportDTO(String postalCode, int numberOfVaccinations, Long countOfCitizens) {
        this.postalCode = postalCode;
        this.numberOfVaccinations = numberOfVaccinations;
        this.countOfCitizens = countOfCitizens;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public int getNumberOfVaccinations() {
        return numberOfVaccinations;
    }

    public Long getCountOfCitizens() {
        return countOfCitizens;
    }
}
