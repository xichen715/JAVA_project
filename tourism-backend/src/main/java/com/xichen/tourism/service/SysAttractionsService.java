package com.xichen.tourism.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xichen.tourism.domain.SysAttractions;

import java.util.List;

public interface SysAttractionsService extends IService<SysAttractions> {

    List<SysAttractions> searchAttractions(String userId);

}
