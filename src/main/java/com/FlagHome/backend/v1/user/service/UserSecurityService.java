package com.FlagHome.backend.v1.user.service;

import com.FlagHome.backend.v1.user.Role;
import com.FlagHome.backend.v1.user.dto.UserDto;
import com.FlagHome.backend.v1.user.entity.User;
import com.FlagHome.backend.v1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private UserRepository userRepository;



    // User에서 UserDto로 변경중
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDto> _user = userRepository.findByusername(username);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        User User = _user.get();

        //분리...ㅠㅠㅠㅠ
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("ADMIN".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.NORMAL.getValue()));
        }
        return new org.springframework.security.core.userdetails.User(User.getName(), User.getPassword(), authorities);
    }
}