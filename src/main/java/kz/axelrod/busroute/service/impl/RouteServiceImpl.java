package kz.axelrod.busroute.service.impl;

import kz.axelrod.busroute.exception.BadRequestException;
import kz.axelrod.busroute.model.dto.ResultDto;
import kz.axelrod.busroute.service.RedisService;
import kz.axelrod.busroute.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class RouteServiceImpl implements RouteService {

    private final RedisService redisService;

    public RouteServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    public ResultDto getResultDto(String from, String to, MultipartFile file) {
        validateInputParams(from, to);
        var resultDto = ResultDto.builder().from(from).to(to).direct(true).build();
        var redisKey = redisService.getKeyForDirectRoute(from, to);
        if (redisService.hasKeyOfData(redisKey)) {
            var isDirect = redisService.getData(redisKey);
            resultDto.setDirect((Boolean) isDirect);
            log.info("RS-getResultDto --- hasKeyOfData, resultDto={}", resultDto);
            return resultDto;
        }
        var routes = getRoutes(file);
        var isDirect = hasDirectRoute(routes, Integer.parseInt(from), Integer.parseInt(to));
        redisService.saveData(redisKey, isDirect);
        resultDto.setDirect(isDirect);
        log.info("RS-getResultDto --- resultDto={}", resultDto);
        return resultDto;
    }

    public boolean hasDirectRoute(Map<Integer, Set<Integer>> graph, int start, int end) {
        log.info("RS-hasDirectRoute --- graph={}, from={}, to={}", graph, start, end);
        // Map of type HashMap - O(1)
        if (graph.containsKey(start)) {
            var entryValue = graph.get(start);
            return entryValue.contains(end);
        }
        return false;
    }

    private Map<Integer, Set<Integer>> getRoutes(List<Integer> fileData) {
        log.info("RS-getRoutes --- fileData {}", fileData);
        var routes = new HashMap<Integer, Set<Integer>>();
        IntStream.range(0, fileData.size()).forEachOrdered(i -> {
            var value = fileData.get(i);
            if (!routes.containsKey(value)) routes.put(value, new HashSet<>());
            IntStream.range(i + 1, fileData.size()).forEachOrdered(j -> routes.get(value).add(fileData.get(j)));
        });
        log.info("RS-getRoutes --- routes {}", routes);
        return routes;
    }

    private Map<Integer, Set<Integer>> getRoutes(MultipartFile file) {
        var routes = new HashMap<Integer, Set<Integer>>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().forEach(line -> {
                var tokens = line.split(" ");
                var row = IntStream.range(1, tokens.length).mapToObj(i -> Integer.parseInt(tokens[i]))
                        .collect(Collectors.toList());
                getRoutes(row).forEach((key, value) -> {
                    if (routes.containsKey(key)) {
                        routes.get(key).addAll(value);
                    } else {
                        routes.put(key, value);
                    }
                });
            });
        } catch (IOException e) {
            log.error("An error occurred while reading the Multipart File.", e);
        }
        return routes;
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
