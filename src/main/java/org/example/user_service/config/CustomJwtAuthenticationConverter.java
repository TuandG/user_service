package org.example.user_service.config;

import org.example.user_service.entity.User;
import org.example.user_service.repository.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;
    private final JwtGrantedAuthoritiesConverter delegate;

    public CustomJwtAuthenticationConverter(UserRepository userRepository) {
        this.userRepository = userRepository;

        this.delegate = new JwtGrantedAuthoritiesConverter();
        this.delegate.setAuthorityPrefix("");
        this.delegate.setAuthoritiesClaimName("scope");
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String username = jwt.getSubject();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isStatus()) {
            throw new RuntimeException("User is inactive");
        }

        Collection<GrantedAuthority> authorities = delegate.convert(jwt);

        return new UsernamePasswordAuthenticationToken(user.getUsername(), jwt, authorities);
    }
}
