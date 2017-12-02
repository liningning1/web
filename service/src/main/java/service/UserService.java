package service;

import bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by linn on 2017/12/2.
 * 用户服务接口
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public User loadUserByUsername(String username)
    {
        return userMapper.loadUserByUsername(username);
    }

    @Transactional
    public void saveUser(User user)
    {
        userMapper.saveUser(user);
    }
    private void getError()
    {
        int i = 1 / 0;
        logger.info("i:{}",i);
    }
}
