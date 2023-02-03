package kz.axelrod.busroute.model.dto;

import lombok.Data;

@Data
public class ResultDto {

    private String from;
    private String to;
    private boolean direct;
}
