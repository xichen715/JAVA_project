package com.xichen.tourism.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xichen.tourism.domain.SysForum;
import com.xichen.tourism.mapper.SysForumMapper;
import com.xichen.tourism.service.SysForumService;
import org.springframework.stereotype.Service;

@Service
public class SysForumServiceImpl extends ServiceImpl<SysForumMapper, SysForum> implements SysForumService {
}
