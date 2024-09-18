package br.edu.unicesumar.prova.restcontroller;

import br.edu.unicesumar.prova.ProvaApplication;
import br.edu.unicesumar.prova.domain.computador.Computador;
import br.edu.unicesumar.prova.domain.computador.ComputadorRepository;
import br.edu.unicesumar.prova.domain.periferico.Periferico;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.SneakyThrows;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProvaApplication.class)
@ContextConfiguration(classes = {ProvaApplication.class})
@WebAppConfiguration
public class ComputadorRestControllerIT extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private ComputadorRepository computadorRepository;

    @BeforeMethod
    protected void beforeMethod() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void testCadastrar() {
        String body = """
                	{
                	"nome": "Notebook Dell",
                	"cor": "Cinza",
                	"dataFabricacao": 2022,
                	"perifericos": [
                		{
                			"nome": "Mouse Logitech"
                		}
                	]
                    }
                """;
        MvcResult result = mockMvc.perform(post("/api/computador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        Computador responseComputador = objectMapper.readValue(contentAsString, Computador.class);

        assertThat(responseComputador).isNotNull();
        assertThat(responseComputador.getNome()).isEqualTo("Notebook Dell");
        assertThat(responseComputador.getCor()).isEqualTo("Cinza");
        assertThat(responseComputador.getDataFabricacao()).isEqualTo(2022);
        assertThat(responseComputador.getPerifericos()).hasSize(1);
    }

    @Test
    public void testFindById() throws Exception {
        Computador computador = createComputador();
        Computador computadorSalvo = computadorRepository.save(computador);
        entityManager_flushClear();
        String contentAsString = mockMvc.perform(get("/api/computador/" + computadorSalvo.getId()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Computador responseComputador = objectMapper.readValue(contentAsString, Computador.class);

        assertThat(responseComputador).isNotNull();
        assertThat(responseComputador.getNome()).isEqualTo("Notebook Dell");
        assertThat(responseComputador.getCor()).isEqualTo("Cinza");
        assertThat(responseComputador.getDataFabricacao()).isEqualTo(2022);
        assertThat(responseComputador.getPerifericos()).hasSize(1);
    }

    @SneakyThrows
    @Test
    public void testEditar() {
        Computador computador = createComputador();
        Computador computadorSalvo = computadorRepository.save(computador);
        entityManager_flushClear();
        computadorSalvo.setNome("Notebook HP");
        computadorSalvo.setCor("Preto");
        computadorSalvo.setDataFabricacao(2023L);
        String contentAsString = mockMvc.perform(put("/api/computador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(computadorSalvo)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Computador responseComputador = objectMapper.readValue(contentAsString, Computador.class);

        assertThat(responseComputador).isNotNull();
        assertThat(responseComputador.getNome()).isEqualTo("Notebook HP");
        assertThat(responseComputador.getCor()).isEqualTo("Preto");
        assertThat(responseComputador.getDataFabricacao()).isEqualTo(2023);
        assertThat(responseComputador.getPerifericos()).hasSize(1);
    }

    @SneakyThrows
    @Test
    public void testDelete() {
        long countAntes = computadorRepository.count();
        assertThat(countAntes).isEqualTo(0);
        String contentAsString = mockMvc.perform(post("/api/computador")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createComputador())))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Computador responseComputador = objectMapper.readValue(contentAsString, Computador.class);
        entityManager_flushClear();
        long countDepoisDeSalvar = computadorRepository.count();
        assertThat(countDepoisDeSalvar).isEqualTo(countAntes + 1);

        mockMvc.perform(delete("/api/computador/" + responseComputador.getId()))
                .andExpect(status().isNoContent());
        entityManager_flushClear();
        long countDepoisDeExcluir = computadorRepository.count();
        assertThat(countDepoisDeExcluir).isEqualTo(countAntes);
    }

    public Computador createComputador() {
        Computador computador = new Computador();
        computador.setNome("Notebook Dell");
        computador.setCor("Cinza");
        computador.setDataFabricacao(2022L);
        computador.addPeriferico(new Periferico("Mouse Logitech", computador));
        return computador;
    }

    protected void entityManager_flushClear() {
        entityManager.flush();
        entityManager.clear();
    }
}