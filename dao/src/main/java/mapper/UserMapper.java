package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import bean.User;
import org.apache.ibatis.annotations.Select;

/**
 * Created by linn on 2017/12/2.
 * 用户数据映射
 */
public interface UserMapper {

    @Select(value="select username,passeord,enabled from users where username=#{username}")
    User loadUserByUsername(@Param("username") String username);

    @Insert(value="insert into users(username,password,enabled,create_date) value(#(username),#(password),#{enabled},#{createDate})")
    void saveUser(User user);
}
