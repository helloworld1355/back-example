package com.example.back_example.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import com.example.back_example.entity.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Configuration
public class MyMvcConfiguration implements WebMvcConfigurer {
    @Value("${image-path}")
    private String uploadPathImg;

    @Value("${kind-upload}")
    private String kindPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:"+uploadPathImg);
        registry.addResourceHandler("/kind/upload/**")
                .addResourceLocations("file:"+kindPath);
    }

    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(500L);
        // 设置数据库类型
        pageInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(pageInterceptor);
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionHandlerInterceptor())
                .addPathPatterns("/user/**")
//                .addPathPatterns("/hisadmin/**")
//                .addPathPatterns("/settings/**")
//                .addPathPatterns("/college/**")
//                .addPathPatterns("/fence/**")
//                .addPathPatterns("/signplan/**")
//                .addPathPatterns("/student/**")
//                .addPathPatterns("/teacher/**")
                .addPathPatterns("/index");
    }

    class SessionHandlerInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            System.out.println("session check");
            User user = (User) request.getSession().getAttribute("user");
            if(user==null){
                // 如果没有登录，重定向到login.html
                response.sendRedirect("/login");
                return false;
            }
            return true;
        }
    }
}
