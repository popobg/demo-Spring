package fr.diginamic.hello.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.repositories.DepartementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DepartementControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private DepartementRepository deptRepository;
    @Autowired
    private MockMvc mockMvc;

    // Data
    private final List<Departement> departements = List.of(new Departement(1, "Ain", "01"), new Departement(2, "Alpes-de-Haute-Provence", "04"));


    @Test
    void testGetDepartementsOk() throws Exception {
        when(deptRepository.findAll()).thenReturn(departements);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/departements/liste")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Ain")))
                .andExpect(jsonPath("$[0].nomDepartement", is("Ain")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nomDepartement", is("Alpes-de-Haute-Provence")))
                .andExpect(jsonPath("$[1].codeDepartement", is("04")));
    }

    @Test
    void testGetDepartementsPaginationOk() throws Exception {
        when(deptRepository.findAllOrderByNom(PageRequest.of(0, 2))).thenReturn(departements);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/departements/liste/pagination?n=2")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomDepartement", is("Ain")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nomDepartement", is("Alpes-de-Haute-Provence")))
                .andExpect(jsonPath("$[1].codeDepartement", is("04")));
    }

    @Test
    void testGetDepartementByIdOk() throws Exception {
        when(deptRepository.findById(1L)).thenReturn(Optional.of(departements.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/departements/{id}", 1L)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("nomDepartement", is("Ain")))
                .andExpect(jsonPath("codeDepartement", is("01")));
    }

    @Test
    void testGetDepartementByCodeOk() throws Exception {
        when(deptRepository.findByCode("04")).thenReturn(Optional.of(departements.getLast()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/departements/code?code=04")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("nomDepartement", is("Alpes-de-Haute-Provence")))
                .andExpect(jsonPath("codeDepartement", is("04")));
    }

    @Test
    void testAddDepartementOk() throws Exception {
        when(deptRepository.findByCode("09")).thenReturn(Optional.empty());
        when(deptRepository.findAll()).thenReturn(departements);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/departements")
                            .content(objectMapper.writeValueAsString(new DepartementDto(0, "09", "Ariège", 0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateVilleOk() throws Exception {
        when(deptRepository.findById(1L)).thenReturn(Optional.of(departements.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/departements/{id}", 1L)
                        .content(objectMapper.writeValueAsString(new DepartementDto(1L, "09", "Ariège", 0)))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteVilleOk() throws Exception {
        when(deptRepository.findById(1L)).thenReturn(Optional.of(departements.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/departements/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
