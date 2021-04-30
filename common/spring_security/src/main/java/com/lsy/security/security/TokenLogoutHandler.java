package com.lsy.security.security;

import com.lsy.utils.R;
import com.lsy.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//退出处理器
//LogoutHandler是security中提供的接口
public class TokenLogoutHandler implements LogoutHandler {


    private TokenManager tokenManager;
    //需要对Redis进行操作。
    private RedisTemplate redisTemplate;


    //通过有参的构造传入需要的两个参数
    public TokenLogoutHandler(TokenManager tokenManager,RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        //1 从header里面获取token
        //2 token不为空，移除token，从redis删除token
        //在Redis中key存的是用户名，value存放的是权限列表。
        //所以要的到key，将其删除
        String token = request.getHeader("token");
        if(token != null) {
            //移除
            tokenManager.removeToken(token);
            //从token获取用户名
            String username = tokenManager.getUserInfoFromToken(token);
            //在Redis中删除
            redisTemplate.delete(username);
        }
        //返回数据
        ResponseUtil.out(response, R.ok());
    }
}
