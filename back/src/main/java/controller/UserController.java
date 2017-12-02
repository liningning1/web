package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import service.UserService;

/**
 * Created by linn on 2017/11/30.
 * 用户访问控制
 */
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(value="/home",method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated")//isAuthenticated 如果用户不是匿名用户就返回true
    public String showHomePage()
    {
        try
        {
            userService.loadUserByUsername("admin");
            logger.info("load user");

        }catch(Exception ex)
        {
           logger.error(ex.getLocalizedMessage(),e);
        }

        return "/index/index";
    }

}
