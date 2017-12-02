package conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linn on 2017/11/30.
 */
//负责安全相关的配置处理
@EnableWebSecurity //@EnableWebSecurity 与WebSecurityConfigurerAdapter 相结合提供基于web的security
//继承了WebSecurityConfigurerAdapter 之后，再加上几行代码，就能实现以下功能：
//1,用户进入应用中的任何url时都需要进行验证
//2,创建一个用户名为：user 密码为password 角色为 ROLE_USER 的用户。
//3,启动基于HTTP Basic和基于表单的验证
@EnableGlobalMethodSecurity(prePostEnabled = true)
//Spring Security默认是禁用注解的，要想开启注解，
// 需要在继承WebSecurityConfigurerAdapter的类上加@EnableGlobalMethodSecurity注解，
// 来判断用户对某个控制层的方法是否具有访问权限
/**
 * 3.1、@EnableGlobalMethodSecurity(securedEnabled=true)
 开启@Secured 注解过滤权限

 3.2、@EnableGlobalMethodSecurity(jsr250Enabled=true)

 开启@RolesAllowed 注解过滤权限

 3.3、@EnableGlobalMethodSecurity(prePostEnabled=true)
 使用表达式时间方法级别的安全性 4个注解可用
 @PreAuthorize 在方法调用之前,基于表达式的计算结果来限制对方法的访问
 @PostAuthorize 允许方法调用,但是如果表达式计算结果为false,将抛出一个安全性异常
 @PostFilter 允许方法调用,但必须按照表达式来过滤方法的结果
 @PreFilter 允许方法调用,但必须在进入方法之前过滤输入值
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    private static Md5PasswordEncoder md5Encoder  = new Md5PasswordEncoder();
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception
    {
        //这是SpringSecurity安全框架自动处理的逻辑，首先是通过username查询users表中是否有记录，然后通过将密码进行MD5加密，
        // 去跟数据库中的密码比对，如果相同就让用户执行configure方法中配置的登陆策略。
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(md5Encoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resource/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //将login.jsp页面定位登录页面,只处理/login这个请求
        http.formLogin().loginPage("/login.jsp").and().formLogin().loginProcessingUrl("/login")
                //如果登录成功就跳转到/home这个地址,如果失败就跳转到/?error=1
        .and().formLogin().defaultSuccessUrl("/home").and().formLogin().failureUrl("/?error=1");
        http.logout().logoutUrl("/logout").and().logout().logoutSuccessUrl("/").and().logout().deleteCookies("JSESSIONID");
        //配置记住我的过期时间
        http.rememberMe().tokenValiditySeconds(1209600).and().rememberMe().rememberMeParameter("remeber-me");
        CharacterEncodingFilter encodeFilter = new CharacterEncodingFilter();
        encodeFilter.setEncoding("utf-8");
        //forceEncoding用来设置是否理会 request.getCharacterEncoding()方法，设置为true则强制覆盖之前的编码格式。
        encodeFilter.setForceEncoding(true);
        http.addFilterBefore(encodeFilter,CsrfFilter.class);//将过滤器 encodeFilter 添加到CsrfFilter 之前.
        http.headers().disable();
        HeaderWriter headerWriter = new HeaderWriter(){
            public void writeHeaders(HttpServletRequest request, HttpServletResponse response)
            {
                response.setHeader("Cache-Control","no-cache,no-store,max-age=0,must-revalidate");
                response.setHeader("Expires","0");
                response.setHeader("Pragma","no-cache");
                response.setHeader("X-Frame-Options","SAMEORIGIN");
                response.setHeader("X-XSS-Protection","1;mode=block");
                response.setHeader("x-content-type-options","nosniff");
            }
        };
        List<HeaderWriter> headerWriterFilterList = new ArrayList<HeaderWriter>();
        headerWriterFilterList.add(headerWriter);
        HeaderWriterFilter headerFilter = new HeaderWriterFilter(headerWriterFilterList);
        http.addFilter(headerFilter);
    }
}
