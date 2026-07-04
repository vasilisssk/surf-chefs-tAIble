package com.chefstable.backend.profile;

import com.chefstable.backend.domain.entity.ClientEntity;
import com.chefstable.backend.profile.dto.ClientResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponse toResponse(ClientEntity client) {
        return new ClientResponse(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getPhone(),
                client.getRegistrationDate(),
                client.getTotalClassesAttended(),
                client.getAllergies(),
                client.isPermanentClient()
        );
    }
}
