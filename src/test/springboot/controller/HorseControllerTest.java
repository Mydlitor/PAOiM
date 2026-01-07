package springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.HorseCondition;
import model.HorseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.dto.HorseRequest;
import springboot.dto.HorseResponse;
import springboot.dto.RatingRequest;
import springboot.service.HorseService;
import springboot.service.RatingService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HorseController.class)
public class HorseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HorseService horseService;

    @MockBean
    private RatingService ratingService;

    private HorseRequest horseRequest;
    private HorseResponse horseResponse;

    @BeforeEach
    void setUp() {
        horseRequest = new HorseRequest(
            "TestHorse", 
            "Arabian", 
            HorseType.HOT_BLOODED, 
            HorseCondition.HEALTHY, 
            5, 
            15000.0, 
            450.0, 
            1L
        );

        horseResponse = new HorseResponse();
        horseResponse.setId(1L);
        horseResponse.setName("TestHorse");
        horseResponse.setBreed("Arabian");
        horseResponse.setType(HorseType.HOT_BLOODED);
        horseResponse.setCondition(HorseCondition.HEALTHY);
        horseResponse.setAge(5);
        horseResponse.setPrice(15000.0);
        horseResponse.setWeightKg(450.0);
        horseResponse.setStableId(1L);
    }

    @Test
    void testAddHorse_Success() throws Exception {
        when(horseService.addHorse(any(HorseRequest.class))).thenReturn(horseResponse);

        mockMvc.perform(post("/api/horse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(horseRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestHorse"));

        verify(horseService, times(1)).addHorse(any(HorseRequest.class));
    }

    @Test
    void testDeleteHorse_Success() throws Exception {
        doNothing().when(horseService).deleteHorse(anyLong());

        mockMvc.perform(delete("/api/horse/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Horse deleted successfully"));

        verify(horseService, times(1)).deleteHorse(1L);
    }

    @Test
    void testGetHorseRating_Success() throws Exception {
        when(ratingService.getAverageRating(anyLong())).thenReturn(4.5);

        mockMvc.perform(get("/api/horse/rating/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.horseId").value(1))
                .andExpect(jsonPath("$.averageRating").value(4.5));

        verify(ratingService, times(1)).getAverageRating(1L);
    }

    @Test
    void testAddRating_Success() throws Exception {
        RatingRequest ratingRequest = new RatingRequest(1L, 5, "Excellent");
        doNothing().when(ratingService).addRating(any(RatingRequest.class));

        mockMvc.perform(post("/api/horse/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Rating added successfully"));

        verify(ratingService, times(1)).addRating(any(RatingRequest.class));
    }
}
