package kz.axelrod.busroute.service;

import kz.axelrod.busroute.model.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
public class RouteService {

    private final RedisService redisService;

    public RouteService(RedisService redisService) {
        this.redisService = redisService;
    }

    public ResultDto getResultDto(String from, String to, MultipartFile file) {
        var fileData = getFileData(file);
        log.info("RS-getResultDto --- from={}, to={}, file-data={}", from, to, fileData);
        var resultDto = getResultDto(from, to);
        resultDto.setDirect(true);
        return resultDto;
    }

    private Map<Integer, List<Integer>> getFileData(MultipartFile file) {
        Map<Integer, List<Integer>> routes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines().map(line -> line.split(" "))
                    .forEachOrdered(tokens -> {
                        int from = Integer.parseInt(tokens[0]);
                        IntStream.range(1, tokens.length).map(i -> Integer.parseInt(tokens[i]))
                                .forEachOrdered(to -> {
                                    if (!routes.containsKey(from)) routes.put(from, new ArrayList<>());
                                    routes.get(from).add(to);
                                });
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
}
