package fr.diginamic.hello.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.models.Departement;
import fr.diginamic.hello.models.Ville;
import fr.diginamic.hello.repositories.DepartementRepository;
import fr.diginamic.hello.repositories.VilleRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class VilleControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private VilleRepository villeRepository;
    @MockitoBean
    private DepartementRepository departementRepository;
    @Autowired
    private MockMvc mockMvc;

    // Data
    private final List<Departement> departements = List.of(new Departement(1, "Ain", "01"), new Departement(2, "Alpes-de-Haute-Provence", "04"));

    private final List<Ville> villes = List.of(new Ville(13497, "Bourg-en-Bresse", 41365, departements.getFirst()),
            new Ville(13721, "Oyonnax", 22559, departements.getFirst()),
            new Ville(13738, "Manosque", 21868, departements.get(1)),
            new Ville(13904, "Digne-les-Bains", 16186, departements.get(1)),
            new Ville(14007, "Ambérieu-en-Bugey", 14081, departements.getFirst()));

    @Test
    void testGetVillesOk() throws Exception {
        when(villeRepository.findAll()).thenReturn(List.of(villes.getFirst()));

        // Response body : [{"id":0,"nom":"Bourg-en-Bresse","codeDepartement":"01","nomDepartement":"Ain","nbHabitants":41365}]
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/liste")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")));
    }

    @Test
    void testGetVillesPaginationOk() throws Exception {
        when(villeRepository.findAllOrderByNom(PageRequest.of(0, 2))).thenReturn(List.of(villes.getFirst(), villes.getLast()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/liste/pagination?n=2")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nom", is("Ambérieu-en-Bugey")))
                .andExpect(jsonPath("$[1].codeDepartement", is("01")));
    }

    @Test
    void testGetVilleByIdOk() throws Exception{
        when(villeRepository.findById(13497L)).thenReturn(Optional.of(villes.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/{id}", 13497L)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("codeDepartement", is("01")));
    }

    @Test
    void testGetVillesByNomOk() throws Exception {
        when(villeRepository.findByNom("Bourg-en-Bresse")).thenReturn(List.of(villes.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/nom/{nom}", "Bourg-en-Bresse")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")));
    }

    @Test
    void testAddVilleOk() throws Exception {
        Departement ain = departements.getFirst();

        when(villeRepository.findByNom(anyString())).thenReturn(new ArrayList<>());
        when(departementRepository.findByCode(anyString())).thenReturn(Optional.of(ain));
        when(villeRepository.findAll()).thenReturn(villes);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/villes")
                        .content(objectMapper.writeValueAsString(new VilleDto(0, "Barcelonnette", 2539, ain.getCode(), ain.getNom())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateVilleOk() throws Exception {
        Departement ain = departements.getFirst();

        when(villeRepository.findById(13497L)).thenReturn(Optional.of(villes.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/villes/{id}", 13497L)
                            .content(objectMapper.writeValueAsString(new VilleDto(13497L, "Bourg en Bresse", 42000, ain.getCode(), ain.getNom())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteVilleOk() throws Exception {
        Departement ain = departements.getFirst();

        when(villeRepository.findById(13497L)).thenReturn(Optional.of(villes.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/villes/{id}", 13497L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetVillesByNomStartingWithOk() throws Exception {
        when(villeRepository.findByNomStartingWith("B")).thenReturn(List.of(villes.getFirst()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/prefixe_nom?prefixe=B")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")));
    }

    @Test
    void testGetVillesByNbHabGreaterThanOk() throws Exception {
        when(villeRepository.findByNbHabitantsGreaterThan(20000)).thenReturn(List.of(villes.getFirst(), villes.get(1), villes.get(2)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/nb_habitants/{min}", 20000)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nom", is("Oyonnax")))
                .andExpect(jsonPath("$[1].codeDepartement", is("01")))
                .andExpect(jsonPath("$[2].nom", is("Manosque")))
                .andExpect(jsonPath("$[2].codeDepartement", is("04")));
    }

    @Test
    void testGetVillesByNbHabBetweenOk() throws Exception {
        when(villeRepository.findByNbHabitantsBetween(20000, 40000)).thenReturn(List.of(villes.get(1), villes.get(2)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/nb_habitants?min=20000&max=40000")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Oyonnax")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nom", is("Manosque")))
                .andExpect(jsonPath("$[1].codeDepartement", is("04")));
    }

    @Test
    void testGetVillesByDepartementAndNbHabGreaterThanOk() throws Exception {
        when(villeRepository.findByDepartementCodeAndNbHabitantsGreaterThan("01", 20000)).thenReturn(List.of(villes.getFirst(), villes.get(1)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/dept_nb_hab/{code_dept}/{min}", "01", 20000)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nom", is("Oyonnax")))
                .andExpect(jsonPath("$[1].codeDepartement", is("01")));
    }

    @Test
    void testGetVillesByDepartmentCodeAndNbHabitantsBetweenOk() throws Exception {
        when(villeRepository.findByDepartementCodeAndNbHabitantsGreaterThan("01", 20000)).thenReturn(List.of(villes.getFirst(), villes.get(1)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/dept_nb_hab/{codeDep}/{min}", "01", 20000)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nom", is("Oyonnax")))
                .andExpect(jsonPath("$[1].codeDepartement", is("01")));
    }

    @Test
    void testGetNVillesByDepartmentCodeOrderByNbHabitantsDescOk() throws Exception {
        when(villeRepository.findByDepartementCodeOrderByNbHabitantsDesc("01", PageRequest.of(0, 2))).thenReturn(List.of(villes.getFirst(), villes.get(1)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/villes/dept_order_nb_hab/{code_dept}?n=2", "01")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom", is("Bourg-en-Bresse")))
                .andExpect(jsonPath("$[0].codeDepartement", is("01")))
                .andExpect(jsonPath("$[1].nom", is("Oyonnax")))
                .andExpect(jsonPath("$[1].codeDepartement", is("01")));
    }
}