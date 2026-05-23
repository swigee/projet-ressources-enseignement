package sae.project.dtos.software;

import java.util.List;

public class SoftwareResponseDto {
    public Integer id;
    public String name;
    public String version;
    public String plugins;
    public String license;
    public String period;
    public String status;
    public String year;
    public List<String> resourceNames;
    public Integer userId;
    public String userFullName;
    public String userEmail;
    public Integer resourceId;
    public String resourceTitle;

    public SoftwareResponseDto(Integer id, String name, String version, String plugins,
                               String license, String period, String status, String year,
                               List<String> resourceNames, Integer userId, String userFullName,
                               String userEmail, Integer resourceId, String resourceTitle) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.plugins = plugins;
        this.license = license;
        this.period = period;
        this.status = status;
        this.year = year;
        this.resourceNames = resourceNames;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
    }
}
