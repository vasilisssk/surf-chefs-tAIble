package com.chefstable.backend.profile;

import com.chefstable.backend.common.CurrentClient;
import com.chefstable.backend.profile.dto.ClientResponse;
import com.chefstable.backend.profile.dto.UpdateProfileRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final CurrentClient currentClient;

    public ProfileController(ProfileService profileService, CurrentClient currentClient) {
        this.profileService = profileService;
        this.currentClient = currentClient;
    }

    @GetMapping
    ClientResponse getProfile() {
        return profileService.getProfile(currentClient.id());
    }

    @PutMapping
    ClientResponse updateProfile(@RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(currentClient.id(), request);
    }
}
