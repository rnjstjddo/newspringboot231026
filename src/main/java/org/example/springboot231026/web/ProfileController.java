package org.example.springboot231026.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profiles(){
        System.out.println("ProfileController의 /profile 진입");
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        System.out.println(profiles.toString());
        List<String> realProfiles= Arrays.asList("real","real1","real2");
        String defaultProfile =profiles.isEmpty() ? "default" : profiles.get(0);

        return profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile);

    }


}
