package com.lemon.mdcord.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemon.mdcord.common.exception.ErrorResponse;
import com.lemon.mdcord.common.exception.UserAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    /**
     * JWT 검증 중 exception 발생 시 response 처리
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (UserAuthenticationException e) {
            setErrorResponse(e, response, HttpStatus.UNAUTHORIZED);
        }
    }

    private void setErrorResponse(Exception ex, HttpServletResponse response, HttpStatus httpStatus) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            ErrorResponse errorResponse = new ErrorResponse(
                    httpStatus.value(),
                    httpStatus.getReasonPhrase(),
                    ex.getLocalizedMessage()
            );
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
        catch(IOException e) {
            log.error("setErrorResponse - IOException");
        }
    }

}
