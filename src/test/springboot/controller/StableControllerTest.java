package springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.dto.HorseResponse;
import springboot.dto.StableRequest;
import springboot.dto.StableResponse;
import springboot.service.HorseService;
import springboot.service.StableService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StableController.class)
public class StableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StableService stableService;

    @MockBean
    private HorseService horseService;

    private StableRequest stableRequest;
    private StableResponse stableResponse;

    @BeforeEach
    void setUp() {
        stableRequest = new StableRequest("TestStable", 10);

        stableResponse = new StableResponse();
        stableResponse.setId(1L);
        stableResponse.setStableName("TestStable");
        stableResponse.setMaxCapacity(10);
        stableResponse.setCurrentOccupancy(3);
    }

    @Test
    void testGetAllStables_Success() throws Exception {
        List<StableResponse> stables = Arrays.asList(stableResponse);
        when(stableService.getAllStables()).thenReturn(stables);

        mockMvc.perform(get("/api/stable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].stableName").value("TestStable"));

        verify(stableService, times(1)).getAllStables();
    }

    @Test
    void testGetStableHorses_Success() throws Exception {
        List<HorseResponse> horses = Arrays.asList(new HorseResponse());
        when(stableService.getStableById(anyLong())).thenReturn(stableResponse);
        when(horseService.getHorsesByStableId(anyLong())).thenReturn(horses);

        mockMvc.perform(get("/api/stable/1"))
                .andExpect(status().isOk());

        verify(stableService, times(1)).getStableById(1L);
        verify(horseService, times(1)).getHorsesByStableId(1L);
    }

    @Test
    void testExportStableHorsesToCSV_Success() throws Exception {
        String csvContent = "ID,Name,Breed,Type,Condition,Age,Price,Weight\n1,TestHorse,Arabian,HOT_BLOODED,HEALTHY,5,15000.0,450.0\n";
        when(stableService.exportStableHorsesToCSV(anyLong())).thenReturn(csvContent);

        mockMvc.perform(get("/api/stable/1/csv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(csvContent));

        verify(stableService, times(1)).exportStableHorsesToCSV(1L);
    }

    @Test
    void testAddStable_Success() throws Exception {
        when(stableService.addStable(any(StableRequest.class))).thenReturn(stableResponse);

        mockMvc.perform(post("/api/stable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stableRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stableName").value("TestStable"));

        verify(stableService, times(1)).addStable(any(StableRequest.class));
    }

    @Test
    void testDeleteStable_Success() throws Exception {
        doNothing().when(stableService).deleteStable(anyLong());

        mockMvc.perform(delete("/api/stable/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Stable deleted successfully"));

        verify(stableService, times(1)).deleteStable(1L);
    }

    @Test
    void testGetStableFill_Success() throws Exception {
        when(stableService.getStableById(anyLong())).thenReturn(stableResponse);
        when(stableService.getStableFillPercentage(anyLong())).thenReturn(30.0);

        mockMvc.perform(get("/api/stable/1/fill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stableId").value(1))
                .andExpect(jsonPath("$.stableName").value("TestStable"))
                .andExpect(jsonPath("$.currentOccupancy").value(3))
                .andExpect(jsonPath("$.maxCapacity").value(10))
                .andExpect(jsonPath("$.fillPercentage").value(30.0));

        verify(stableService, times(1)).getStableById(1L);
        verify(stableService, times(1)).getStableFillPercentage(1L);
    }
}
