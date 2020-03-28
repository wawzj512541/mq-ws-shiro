package com.example.shiro.common.shiro;

import com.alibaba.fastjson.JSONObject;
import com.example.shiro.common.result.APIResponse;
import com.example.shiro.common.result.CodeMsg;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroLoginFilter extends FormAuthenticationFilter {
    /**
     * 如果isAccessAllowed返回false 则执行onAccessDenied
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     *
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //这里是个坑，如果不设置的接受的访问源，那么前端都会报跨域错误，因为这里还没到corsConfig里面
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                // 放行 allow them to see the login page ;)
                return true;
            }
        } else {
//            if (isAjaxRequest(WebUtils.toHttp(request))) {
//                httpServletResponse.getWriter().write(JSONObject.toJSON(APIResponse.returnFail(CodeMsg.MUST_LOGIN_ERROP)).toString());
//            } else {
//                saveRequestAndRedirectToLogin(request, response);
//            }
            httpServletResponse.getWriter().write(JSONObject.toJSON(APIResponse.returnFail(CodeMsg.LOGOUT_ERROR)).toString());
            return false;
        }
    }

    /**
     * 判断是否是ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}

