package com.xichen.tourism.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xichen.tourism.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    Page<User> getUserPage(Page<User> page, @Param("ew") User apeUser);

}
