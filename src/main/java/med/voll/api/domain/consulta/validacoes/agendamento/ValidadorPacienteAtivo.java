package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private PacienteRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        if(dados.idPaciente() == null) {
            return;
        }

        var pacienteEstadoAtivo = repository.findAtivoById(dados.idPaciente());

        if(!pacienteEstadoAtivo) {
            throw new ValidacaoException("Consulta não pode ser agendada com médico não ativo");
        }
    }
}
