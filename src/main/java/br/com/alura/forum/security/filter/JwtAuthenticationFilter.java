package br.com.alura.forum.security.filter;

import br.com.alura.forum.security.jwt.TokenManager;
import br.com.alura.forum.security.service.UsersService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_TYPE = "Bearer ";

    private TokenManager tokenManager;

    private UsersService usersService;

    public JwtAuthenticationFilter(TokenManager tokenManager, UsersService usersService) {
        this.tokenManager = tokenManager;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getTokenFromRequest(request);

        Optional<UsernamePasswordAuthenticationToken> someUserToken = tokenManager.getIfValid(jwt, (j) -> {
            Claims claims = (Claims) j.getBody();
            return Long.parseLong(claims.getSubject());
        }).map(usersService::loadUserById)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()));

        if (someUserToken.isPresent()) {
            UsernamePasswordAuthenticationToken uToken = someUserToken.get();
            SecurityContextHolder.getContext().setAuthentication(uToken);
        }

        filterChain.doFilter(request, response);


    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_TYPE)) {
            return bearerToken.substring(TOKEN_TYPE.length());
        }
        return null;
    }
}
