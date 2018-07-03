package com.example.passportdemo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DemoFilter
 *
 * @author muxiaorui
 * @create 2018-06-25 15:46
 **/
public class DemoFilter extends HttpServlet implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("DemoFilter init begin=======");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)servletRequest;
        HttpServletResponse res=(HttpServletResponse)servletResponse;
        req.setAttribute("Content-Type","application/json; charset=UTF-8");
        System.out.println( req.getHeaderNames());
        System.out.println( req.getParameterMap());
        filterChain.doFilter(req,res);
    }

    @Override
    public void destroy() {
        System.out.println("DemoFilter init destroy=======");
    }
}
