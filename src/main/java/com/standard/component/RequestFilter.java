package com.standard.component;

import com.alibaba.fastjson.JSON;

import com.manage.contract.utils.CustomException;
import com.standard.pojo.DataVO;
import com.standard.utils.StringUtil;
import com.standard.utils.XmlMapUtil;
import com.standard.wrapper.RequestWrapper;
import com.standard.wrapper.ResponseWrapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * controller全局异常处理  日志打印
 */
@WebFilter(urlPatterns={"/api/*"},filterName = "requestFilter")
public class RequestFilter implements Filter{

    private final Logger logger = LoggerFactory.getLogger(RequestFilter.class);


    /**
     * 日志打印
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
//    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");

        // 包装request和response
        RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);

        // 入参日志
        String path = request.getQueryString();
        String servletPath = request.getServletPath();
        String url = request.getRequestURI();
        String[] statics = {".css", ".html", ".jsp", ".js", ".png", ".jpg"};
        for (String suffix : statics) {
            if (servletPath.contains(suffix)){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }
        logger.info("<<<=== request Url:" + url + " & queryString:" + path + " & servletPath:" + servletPath);
        logger.info("<<<=== request ContentType: {} Uri: {}", request.getContentType(), request.getRequestURI());
        logger.info("<<<=== request Param:\n {}", IOUtils.toString(requestWrapper.getInputStream()));
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        DataVO dataVO = null;
        ArrayList<String> list = new ArrayList<>();
        list.add("/contract/getContractWord");
        list.add("/contract/getContractFile");
        list.add("/user/login");
        list.add("/contractTemplet/getTemplateWord");
        list.add("/contractTemplet/officeCallback");
        list.add("/file/getFileWord");
//
//        if (user == null && !list.contains(request.getRequestURI())){
//            dataVO = new DataVO();
//            dataVO.setCode(304);
//            dataVO.setMessage("请登录后使用");
//        } else {
//            filterChain.doFilter(requestWrapper, responseWrapper);
//        }


        String result = new String(responseWrapper.getResponseData(), StringUtil.DEFAULT_CHARTSET);
        ServletOutputStream outputStream = response.getOutputStream();

        if (dataVO != null){
            result = JSON.toJSONString(dataVO);
        }

        // 出参日志
        logger.info("===>>> response ContentType: {} Status: {} ", response.getContentType(), response.getStatus());
        logger.info("===>>> response ReturnData:\n {} ", result);

        // 真正返回数据
        outputStream.write(result.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
