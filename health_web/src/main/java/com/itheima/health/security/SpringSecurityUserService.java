package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author fanbo
 * @date 2020/8/2 12:03
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void initUserData(User user){
        user.setUsername("haha");
        user.setPassword(bCryptPasswordEncoder.encode("123456"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
/*        User user = new User();
        initUserData(user);
        userService.add(user);*/
        User user = userService.findUserByUsername(username);
        if (user!=null){
            String password = user.getPassword();
            //权限集合
            List<GrantedAuthority> authorities = new ArrayList<>();

            SimpleGrantedAuthority sga = null;
            //授权
            Set<Role> roles = user.getRoles();
            if (roles!=null){
                for (Role role : roles) {
                    //授予角色   getKeyword角色关键字
                  sga = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sga);

                    //角色下权限
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions!=null){
                        for (Permission permission : permissions) {
                            //授予权限
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(username,password,authorities);
        }
        return null;
    }
}
