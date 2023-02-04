package kz.axelrod.busroute.service;

import kz.axelrod.busroute.exception.BadRequestException;
import kz.axelrod.busroute.model.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
@Service
public class RouteService {

    private final RedisService redisService;

    public RouteService(RedisService redisService) {
        this.redisService = redisService;
    }

    public ResultDto getResultDto(String from, String to, MultipartFile file) {
        validateInputParams(from, to);
        var resultDto = getResultDto(from, to);
        var redisKey = redisService.getKeyForDirectRoute(from, to);
        if (redisService.hasKeyOfData(redisKey)) {
            var isDirect = redisService.getData(redisKey);
            resultDto.setDirect((Boolean) isDirect);
            log.info("RS-getResultDto --- hasKeyOfData, resultDto={}", resultDto);
            return resultDto;
        }
        var fileData = getFileData(file);
        var isDirect = hasDirectRoute(fileData, Integer.parseInt(from), Integer.parseInt(to));
        log.info("IS: {}", isDirect);
        redisService.saveData(redisKey, isDirect);
        resultDto.setDirect(isDirect);
        log.info("RS-getResultDto --- resultDto={}", resultDto);
        return resultDto;
    }

    public static boolean hasDirectRoute(Map<Integer, List<Integer>> graph, int start, int end) {
        log.info("RS-hasDirectRoute --- graph={}, from={}, to={}", graph, start, end);
        return graph.values().stream().anyMatch(list -> list.contains(start) && list.contains(end));
    }

    private Map<Integer, List<Integer>> getFileData(MultipartFile file) {
        var routes = new HashMap<Integer, List<Integer>>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().map(line -> line.split(" "))
                    .forEachOrdered(tokens -> {
                        var from = Integer.parseInt(tokens[0]);
                        var toStops = routes.computeIfAbsent(from, k -> new ArrayList<>());
                        IntStream.range(1, tokens.length).mapToObj(i -> Integer.parseInt(tokens[i]))
                                .forEachOrdered(toStops::add);
                    });
        } catch (IOException e) {
            log.error("An error occurred while reading the Multipart File.", e);
        }
        log.info("RS-getFileData --- routes {}", routes);
        return routes;
    }

    private ResultDto getResultDto(String from, String to) {
        var resultDto = new ResultDto();
        resultDto.setFrom(from);
        resultDto.setTo(to);
        resultDto.setDirect(false);
        return resultDto;
    }

    private void validateInputParams(String from, String to) {
        try {
            Integer.parseInt(from);
            Integer.parseInt(to);
        } catch (Exception e) {
            // NumberFormatException
            throw new BadRequestException("Invalid input. Both 'from' and 'to' must be valid integers");
        }
    }
}
