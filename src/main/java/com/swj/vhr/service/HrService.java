package com.swj.vhr.service;

import com.swj.vhr.mapper.HrMapper;
import com.swj.vhr.model.Hr;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: swj
 * @Description:
 * @Date: Create in 11:16 2021/1/13
 */
@Service
public class HrService implements UserDetailsService {

    @Resource
    private HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username);
        if (hr == null){
            throw new UsernameNotFoundException("用户名不存在！！");
        }
        return hr;
    }
}
