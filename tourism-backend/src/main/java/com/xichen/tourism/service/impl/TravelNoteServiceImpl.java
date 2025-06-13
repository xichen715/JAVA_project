package com.xichen.tourism.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xichen.tourism.domain.TravelNote;
import com.xichen.tourism.mapper.TravelNoteMapper;
import com.xichen.tourism.service.TravelNoteService;
import org.springframework.stereotype.Service;

@Service
public class TravelNoteServiceImpl extends ServiceImpl<TravelNoteMapper, TravelNote> implements TravelNoteService {
    // 可根据需要扩展自定义方法
}