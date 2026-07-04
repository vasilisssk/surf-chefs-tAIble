package com.chefstable.backend.profile;

import com.chefstable.backend.common.NotFoundException;
import com.chefstable.backend.domain.entity.ClientEntity;
import com.chefstable.backend.domain.repository.ClientRepository;
import com.chefstable.backend.profile.dto.ClientResponse;
import com.chefstable.backend.profile.dto.UpdateProfileRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ProfileService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Transactional(readOnly = true)
    public ClientResponse getProfile(String clientId) {
        return clientMapper.toResponse(findClient(clientId));
    }

    @Transactional
    public ClientResponse updateProfile(String clientId, UpdateProfileRequest request) {
        ClientEntity client = findClient(clientId);
        if (request.firstName() != null) {
            client.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            client.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            client.setPhone(request.phone());
        }
        if (request.allergies() != null) {
            client.setAllergies(request.allergies());
        }
        return clientMapper.toResponse(client);
    }

    private ClientEntity findClient(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));
    }
}
