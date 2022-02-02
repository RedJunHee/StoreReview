package com.review.storereview.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req =  (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse)  response;
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        res.setHeader("Access-Control-Allow-Credentials","true");
        res.setHeader("Access-Control-Allow-Methods","*");
        res.setHeader("Access-Control-Allow-Headers","Origin, Content-Type, Accept, X-Requested-With, Authorization");
        res.setHeader("Access-Control-Max-Age","3600");
        System.out.println("Cors Response Header 요청 : " + req.getHeader("Origin"));
        if("OPTIONS".equalsIgnoreCase(req.getMethod()))
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
        else
            chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
