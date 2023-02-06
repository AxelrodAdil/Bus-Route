package kz.axelrod.busroute.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDto {

    private String from;
    private String to;
    private boolean direct;
}
