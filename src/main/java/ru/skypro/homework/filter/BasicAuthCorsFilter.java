package ru.skypro.homework.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.skypro.homework.utils.LogShifter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(BasicAuthCorsFilter.class);
    private final LogShifter shifter = LogShifter.getLogShifter();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        shifter.shiftLog(logger,"Запущен метод фильтра doFilterInternal {}, {}, {},{},{},{}"
                , httpServletRequest.getHeaderNames()
                , httpServletRequest.getAuthType()
                , httpServletRequest.getContextPath()
                , httpServletRequest.getMethod()
                , httpServletResponse.getHeaderNames()
                , filterChain
        );

        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        shifter.shiftBackLog(logger,"Выполнен метод фильтра doFilterInternal");
    }
}