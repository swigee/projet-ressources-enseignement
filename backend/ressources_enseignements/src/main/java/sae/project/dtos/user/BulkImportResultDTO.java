package sae.project.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkImportResultDTO {
    private int successCount;
    private int errorCount;
    private List<String> errors;
}
