package com.pulga.ratelimiter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimiter implements Filter {
    private final RedisTemplate<String, Integer> redisTemplate;

    public RateLimiter(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String param = servletRequest.getParameter("API-KEY");
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String path = httpServletRequest.getRequestURI();
        String ipAddress = httpServletRequest.getRemoteAddr();
        if(param == null){
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write("unauthorized");
            return;
        }
        Integer count = redisTemplate.opsForValue().get(param);

        if(count == null){
            redisTemplate.opsForValue().setIfAbsent(param, 1, Duration.ofSeconds(10));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if(count >= 3){
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpServletResponse.getWriter().write("too many requests");
        } else{
            redisTemplate.opsForValue().increment(param);
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
