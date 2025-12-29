package sae.project.dtos;

public class ServicesSummaryDto {
    private String resourceTitle;
    private int hoursCM;
    private int hoursTD;
    private int hoursTP;
    private int totalHours;

    public ServicesSummaryDto() {}

    public ServicesSummaryDto(String resourceTitle, int hoursCM, int hoursTD, int hoursTP) {
        this.resourceTitle = resourceTitle;
        this.hoursCM = hoursCM;
        this.hoursTD = hoursTD;
        this.hoursTP = hoursTP;
        this.totalHours = hoursCM + hoursTD + hoursTP;
    }


    public String getResourceTitle() {
        return resourceTitle;
    }
    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public int getHoursCM() {
        return hoursCM;
    }
    public void setHoursCM(int hoursCM) {
        this.hoursCM = hoursCM;
    }

    public int getHoursTD() {
        return hoursTD;
    }
    public void setHoursTD(int hoursTD) {
        this.hoursTD = hoursTD;
    }

    public int getHoursTP() { return hoursTP; }
    public void setHoursTP(int hoursTP) {
        this.hoursTP = hoursTP;
    }

    public int getTotalHours() {
        return totalHours;
    }
    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }


}
