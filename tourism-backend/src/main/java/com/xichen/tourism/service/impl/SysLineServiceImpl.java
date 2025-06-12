package com.xichen.tourism.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xichen.tourism.domain.SysLine;
import com.xichen.tourism.mapper.SysLineMapper;
import com.xichen.tourism.service.SysLineService;
import org.springframework.stereotype.Service;

@Service
public class SysLineServiceImpl extends ServiceImpl<SysLineMapper, SysLine> implements SysLineService {
}
