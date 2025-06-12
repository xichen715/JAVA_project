package com.xichen.tourism.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xichen.tourism.domain.SysComments;
import com.xichen.tourism.mapper.SysCommentsMapper;
import com.xichen.tourism.service.SysCommentsService;
import org.springframework.stereotype.Service;

@Service
public class SysCommentsServiceImpl extends ServiceImpl<SysCommentsMapper, SysComments> implements SysCommentsService {
}
