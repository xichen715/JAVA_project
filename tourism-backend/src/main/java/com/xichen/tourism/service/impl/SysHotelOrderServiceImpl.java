package com.xichen.tourism.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xichen.tourism.domain.SysHotelOrder;
import com.xichen.tourism.mapper.SysHotelOrderMapper;
import com.xichen.tourism.service.SysHotelOrderService;
import org.springframework.stereotype.Service;

@Service
public class SysHotelOrderServiceImpl extends ServiceImpl<SysHotelOrderMapper, SysHotelOrder> implements SysHotelOrderService {
}
