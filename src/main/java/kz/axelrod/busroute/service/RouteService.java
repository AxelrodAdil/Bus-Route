package kz.axelrod.busroute.service;

import kz.axelrod.busroute.model.dto.ResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

public interface RouteService {

    ResultDto getResultDto(String from, String to, MultipartFile file);

    boolean hasDirectRoute(Map<Integer, Set<Integer>> graph, int start, int end);
}
