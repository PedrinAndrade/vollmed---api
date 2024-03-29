package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.DadosDetalhamentoPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PacienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroPaciente> dadosCadastroPacienteJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoPaciente> dadosDetalhamentoPacienteJson;

    @Autowired
    private PacienteRepository repository;

    @Test
    @DisplayName("Deveria devolver o código http 400 quando as informações estão inválidas")
    @WithMockUser
    void cadastrarCenario1() throws Exception {
        var response = mvc.perform(post("/pacientes")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver o código http 200 quando as informações estão inválidas")
    @WithMockUser
    void cadastrarCenario2() throws Exception {
        var dadosCadastro = new DadosCadastroPaciente(
                "Paciente",
                "paciente@email.com",
                "000000000",
                "00011122233",
                dadosEndereco()
        );

        when(repository.save(any())).thenReturn(new Paciente(dadosCadastro));
        var response = mvc.perform(post("/pacientes")).andReturn().getResponse();

        var dadosDetalhamento = new DadosDetalhamentoPaciente(
                null,
                dadosCadastro.nome(),
                dadosCadastro.email(),
                dadosCadastro.cpf(),
                dadosCadastro.telefone(),
                new Endereco(dadosCadastro.endereco()),
                true
        );
        var jsonEsperado = dadosDetalhamentoPacienteJson.write(dadosDetalhamento).getJson();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "São Paulo",
                "SP",
                null,
                null
        );
    }

}