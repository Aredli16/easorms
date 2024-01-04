package fr.aredli.easorms.registration.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
	@Override
	public Collection<GrantedAuthority> convert(@NonNull Jwt source) {
		Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
		
		List<?> roles = (List<?>) realmAccess.get("roles");
		
		return roles.stream().map(role -> "ROLE_" + role.toString().toUpperCase()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
}
