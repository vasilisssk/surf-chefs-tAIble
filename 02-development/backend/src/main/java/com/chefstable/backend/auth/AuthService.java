package com.chefstable.backend.auth;

import com.chefstable.backend.auth.dto.AuthResponse;
import com.chefstable.backend.auth.dto.LoginRequest;
import com.chefstable.backend.auth.dto.RegistrationRequest;
import com.chefstable.backend.common.ConflictException;
import com.chefstable.backend.common.IdGenerator;
import com.chefstable.backend.common.UnauthorizedException;
import com.chefstable.backend.domain.entity.ClientEntity;
import com.chefstable.backend.domain.repository.ClientRepository;
import com.chefstable.backend.profile.ClientMapper;
import com.chefstable.backend.security.JwtService;
import java.time.OffsetDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final IdGenerator idGenerator;

    public AuthService(ClientRepository clientRepository, ClientMapper clientMapper, PasswordEncoder passwordEncoder, JwtService jwtService, IdGenerator idGenerator) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public AuthResponse register(RegistrationRequest request) {
        if (clientRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered");
        }
        if (clientRepository.existsByPhone(request.phone())) {
            throw new ConflictException("Phone is already registered");
        }
        ClientEntity client = new ClientEntity(
                idGenerator.prefixed("client"),
                request.firstName(),
                request.email(),
                request.phone(),
                passwordEncoder.encode(request.password()),
                OffsetDateTime.now()
        );
        ClientEntity saved = clientRepository.save(client);
        return new AuthResponse(jwtService.generateToken(saved.getId()), clientMapper.toResponse(saved));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        ClientEntity client = clientRepository.findByEmail(request.email())
                .or(() -> clientRepository.findByPhone(request.email()))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), client.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return new AuthResponse(jwtService.generateToken(client.getId()), clientMapper.toResponse(client));
    }
}
