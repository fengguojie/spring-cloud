package com.jellard.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class MyFilter extends ZuulFilter{
	private static Logger log = LoggerFactory.getLogger(MyFilter.class);
	
	/**
	 * filterType：返回一个字符串代表过滤器的类型，
	 * 在zuul中定义了四种不同生命周期的过滤器类型，具体如下： 
	 * pre：路由之前
	 * routing：路由之时
	 * post： 路由之后
	 * error：发送错误调用
	 */
    @Override
    public String filterType() {
        return "pre";
    }
    
    /**
     * filterOrder：过滤的顺序
     */
    @Override
    public int filterOrder() {
        return 0;
    }
    
	/**
	 * shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
	 */
    @Override
    public boolean shouldFilter() {
        return true;
    }
    /**
     * run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(accessToken == null) {
            
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){
            	log.error(e.getMessage());
            }
            log.info("token is empty");
        }else {
			ctx.setSendZuulResponse(true);
			ctx.setResponseStatusCode(200);
			ctx.set("isSuccess", true);
			log.info("ok");
		}
        return null;
    }

}
