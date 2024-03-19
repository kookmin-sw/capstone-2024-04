package com.drm.server.service;

import com.drm.server.domain.user.CustomUserDetails;
import com.drm.server.domain.user.CustomUserInfoDto;
import com.drm.server.domain.user.User;
import com.drm.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository usersRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = usersRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없다"));
        CustomUserInfoDto userInfoDto = modelMapper.map(user, CustomUserInfoDto.class);
        return new CustomUserDetails(userInfoDto);
    }
}
