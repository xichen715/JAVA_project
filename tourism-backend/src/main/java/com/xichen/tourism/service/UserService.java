package com.xichen.tourism.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xichen.tourism.domain.User;

public interface UserService extends IService<User> {

    Page<User> getUserPage(User apeUser);

}
