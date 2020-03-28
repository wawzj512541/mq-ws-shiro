package com.example.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.web.dao.SysLogDao;
import com.example.web.entity.SysLogEntity;
import com.example.web.service.SysLogService;
import org.springframework.stereotype.Service;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

}
