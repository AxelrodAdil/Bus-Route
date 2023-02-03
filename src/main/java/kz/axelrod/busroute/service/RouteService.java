package kz.axelrod.busroute.service;

import kz.axelrod.busroute.model.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class RouteService {

    private final RedisService redisService;

    public RouteService(RedisService redisService) {
        this.redisService = redisService;
    }

    public ResultDto getResultDto(String from, String to, MultipartFile file){
        log.info("ROUTE-SERVICE --- from={}, to={}, file={}", from, to, file);
        var resultDto = getResultDto(from, to);
        resultDto.setDirect(true);
        return resultDto;
    }

    private ResultDto getResultDto(String from, String to){
        var resultDto = new ResultDto();
        resultDto.setFrom(from);
        resultDto.setTo(to);
        resultDto.setDirect(false);
        return resultDto;
    }
}
