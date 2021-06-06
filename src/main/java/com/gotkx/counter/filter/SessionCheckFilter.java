package com.gotkx.counter.filter;


import com.google.common.collect.Sets;
import com.gotkx.counter.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SessionCheckFilter implements Filter {

    @Autowired
    private AccountService accountService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private Set<String> whiteRootPaths = Sets.newHashSet(
            "login","msgsocket","test"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 解决跨域问题
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 放行请求
        //filterChain.doFilter(servletRequest,servletResponse);

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //http://localhost:8090/login/pwdsetting
        //  /login/pwdsetting
        String path = request.getRequestURI();
        String[] split = path.split("/");
        if(split.length < 2){
            request.getRequestDispatcher(
                    "/login/loginfail"
            ).forward(servletRequest, servletResponse);
        }else {
            if(!whiteRootPaths.contains(split[1])){
                //不在白名单 验证token
                if(accountService.accountExistInCache(request.getParameter("token"))){
                    filterChain.doFilter(servletRequest, servletResponse);
                }else {
                    request.getRequestDispatcher("/login/loginfail").forward(servletRequest, servletResponse);
                }
            }else {
                //在白名单 放行
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
