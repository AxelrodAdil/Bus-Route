package kz.axelrod.busroute.controller;

import kz.axelrod.busroute.exception.UnauthorizedException;
import kz.axelrod.busroute.model.dto.ApiResponse;
import kz.axelrod.busroute.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/direct")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleError(Exception e) {
        log.error("Caught unhandled exception", e);
        String message = e.getMessage();
        int status = e instanceof UnauthorizedException ? 401 : 400;
        return ResponseEntity.status(status).body(ApiResponse.fail(message));
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<ApiResponse<?>> getResult(@RequestPart("from") String from,
                                                   @RequestPart("to") String to,
                                                   @RequestPart("file") MultipartFile file) {
        var response = routeService.getResultDto(from, to, file);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
