package sae.project.dtos.ticket;

public class TicketDto {
    public String title;
    public String description;
    @com.fasterxml.jackson.annotation.JsonAlias("iduser")
    public Integer userId;
}