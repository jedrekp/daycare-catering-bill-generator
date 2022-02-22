package jedrekp.daycarecateringbillgenerator.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.http.request.header}")
    private String authorizationHeader;

    @Value("${jwt.get.token.uri}")
    private String authenticationPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (request.getMethod().equals("OPTIONS") || request.getRequestURI().equals(authenticationPath)) {
            chain.doFilter(request, response);
            return;
        }

        log.info("Authentication Request For '{}'", request.getRequestURL());
        String requestTokenHeader = request.getHeader(this.authorizationHeader);
        Principal principal = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            principal = jwtTokenUtil.decodeToken(requestTokenHeader.substring(7));
        } else {
            log.warn("Authorization header does not start with 'Bearer ' or is missing");
        }

        if (principal != null) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(principal.getUsername(), null, principal.getRoles());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        chain.doFilter(request, response);
    }

}
