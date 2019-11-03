package com.en.ims.system.service;


import com.en.ims.system.entity.SystemPermission;
import com.en.ims.system.entity.SystemUser;
import com.en.ims.system.repository.PermissionRepository;
import com.en.ims.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    public Page<SystemUser> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = userRepository.findByUserName(username);
        if (user != null) {
            List<SystemPermission> permissions = permissionRepository.findByUserId(user.getId());
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (SystemPermission permission : permissions) {
                if (permission != null && permission.getPermissionname()!=null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getPermissionname());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encode = passwordEncoder.encode(user.getPassWord());
            return new User(user.getUserName(), user.getPassWord(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("用户名: " + username + " 不存在!");
        }
    }
}
