package sae.project.dtos.ticket;

import java.util.Date;

public class TicketResponseDto {

    public Integer id;
    public String title;
    public String description;
    public Date date;
    public Date resolutionDate;
    public String status;
    public Integer userId;
    public String userName;

    public TicketResponseDto() {
    }

    public TicketResponseDto(Integer id, String title, String description, Date date, Date resolutionDate,
            String status, Integer userId,
            String userName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.resolutionDate = resolutionDate;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
    }
}
