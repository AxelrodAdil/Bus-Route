package kz.axelrod.busroute.service;

import kz.axelrod.busroute.model.dto.ResultDto;
import kz.axelrod.busroute.service.impl.RouteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RouteServiceTest {

    private static final int start = 6;
    private static final int end = 4;
    private static final String startStr = "6";
    private static final String endStr = "4";
    private ResultDto resultDto;

    @Spy
    @InjectMocks
    private RouteServiceImpl routeService;

    @Mock
    Map<Integer, Set<Integer>> graph;
    @Mock
    private RedisService redisService;

    @BeforeEach
    public void init() {
        resultDto = ResultDto.builder()
                .from(startStr)
                .to(endStr)
                .direct(true)
                .build();
    }

    @Test
    public void testGetResultDtoHasKeyOfData() {
        MultipartFile file = mock(MultipartFile.class);

        when(redisService.getKeyForDirectRoute(startStr, endStr)).thenReturn("A-Z");
        when(redisService.hasKeyOfData("A-Z")).thenReturn(true);
        when(redisService.getData("A-Z")).thenReturn(true);
        ResultDto actualResultDto = routeService.getResultDto(startStr, endStr, file);

        verify(redisService).getKeyForDirectRoute(startStr, endStr);
        verify(redisService).hasKeyOfData("A-Z");
        verify(redisService).getData("A-Z");
        verify(redisService, never()).saveData(anyString(), any());
        assertEquals(resultDto, actualResultDto);
    }

    @Test
    void testHasDirectRouteValidRoute() {
        Set<Integer> values = new HashSet<>(Arrays.asList(4, 5));
        when(graph.containsKey(start)).thenReturn(true);
        when(graph.get(start)).thenReturn(values);
        boolean result = routeService.hasDirectRoute(graph, start, end);
        assertTrue(result);
    }

    @Test
    public void testHasDirectRouteInvalidRoute() {
        when(graph.containsKey(start)).thenReturn(false);
        boolean result = routeService.hasDirectRoute(graph, start, end);
        assertFalse(result);
    }
}