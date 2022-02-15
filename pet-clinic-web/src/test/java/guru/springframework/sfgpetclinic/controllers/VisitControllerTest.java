package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {
    private static final String VISIT_URL = "/owners/1/pets/2/visits/new";

    @Mock
    VisitService visitService;

    @Mock
    PetService petService;

    @InjectMocks
    VisitController visitController;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Pet pet = new Pet();
        when(petService.findById(anyLong())).thenReturn(pet);

        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();
    }

    @Test
    void initNewVisitForm() throws Exception {
        mockMvc.perform(get(VISIT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("visit"))
                .andExpect(model().attributeExists("pet"))
                .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    void addNewvisit() throws Exception {
        mockMvc.perform(post(VISIT_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("visit"))
                .andExpect(view().name("redirect:/owners/1"));

        verify(visitService).save(any());
    }

    @Test
    void addNewvisitWithDate() throws Exception {
        mockMvc.perform(post(VISIT_URL)
                .param("date", "2022-02-15")
                .param("description", "test with date!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("visit"))
                .andExpect(view().name("redirect:/owners/1"));
    }
}