package br.com.softdesign.desafio.service;

import br.com.softdesign.desafio.model.Associate;
import br.com.softdesign.desafio.repository.AssociateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Serviço de negócio responsável por gerenciar o ciclo de vida dos associados.
 */
@Service
public class AssociateService {

    private final AssociateRepository associateRepository;

    public AssociateService(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    /**
     * Cadastra um novo associado no sistema.
     * @param associate O objeto Associate a ser salvo.
     * @return O associado salvo com o ID gerado.
     * @throws IllegalArgumentException Se o CPF já estiver cadastrado.
     */
    @Transactional
    public Associate registerAssociate(Associate associate) {
        // 1. Validação de Negócio: Verificar duplicidade de CPF
        Optional<Associate> existingAssociate = associateRepository.findByCpf(associate.getCpf());
        if (existingAssociate.isPresent()) {
            throw new IllegalArgumentException("Associado já cadastrado com este CPF.");
        }

        // 2. Persistência
        return associateRepository.save(associate);
    }

    /**
     * Busca um associado pelo CPF.
     * @param cpf O CPF do associado.
     * @return Optional contendo o associado, se encontrado.
     */
    public Optional<Associate> findByCpf(String cpf) {
        return associateRepository.findByCpf(cpf);
    }
}
