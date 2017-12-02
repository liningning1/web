package conf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by linn on 2017/11/29.
 * 负责处理SpringMvc的配置信息以及组件的加载
 */
//WebMvcConfigurerAdapter 进行SpringMVC的一些配置
@EnableWebMvc
@EnableAspectJAutoProxy
@EnableScheduling //@EnableScheduling来开启对计划任务的支持，然后在要执行计划任务的方法上注解@Scheduled，声明这是一个计划任务。
@ComponentScan(basePackages ={"com.aitongyi.web.back.controller","com.aitongyi.web.service"})
@MapperScan("com.aitongyi.web.dao.mapper")
//1 使用mybatis注解需要的配置。如下面的代码所示，使用@MapperScan来扫描注册mybatis数据库接口类，其中basePackages属性表明接口类所在的包，sqlSessionTemplateRef表明接口类使用的SqlSessionTemplate。如果项目需要配置两个数据库，@MapperScan也需要分别配置。

public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    /**
     * jsp 视图解析器
     */
    @Bean
    public InternalResourceViewResolver jspViewResolver()
    {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        bean.setPrefix("/WEB-INF/pages/");
        bean.setSuffix(".jsp");
        return bean;
    }
    /**
     * 公共部分解析器
     */
    public CommonsMultipartResolver commonsMultipartResolver()
    {
        CommonsMultipartResolver common = new CommonsMultipartResolver();
        common.setMaxUploadSize(10 * 1024 * 1024);//10M
        return common;
    }
}
