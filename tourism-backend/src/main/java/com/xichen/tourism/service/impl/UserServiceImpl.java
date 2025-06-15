package com.xichen.tourism.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xichen.tourism.domain.User;
import com.xichen.tourism.mapper.UserMapper;
import com.xichen.tourism.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Page<User> getUserPage(User user) {
        Page<User> page = new Page<>(user.getPageNumber(),user.getPageSize());
        return baseMapper.getUserPage(page,user);
    }
}
