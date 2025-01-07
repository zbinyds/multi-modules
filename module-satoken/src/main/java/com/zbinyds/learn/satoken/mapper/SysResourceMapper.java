package com.zbinyds.learn.satoken.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbinyds.learn.satoken.model.entity.SysResource;

public interface SysResourceMapper extends BaseMapper<SysResource> {
    int deleteByPrimaryKey(Long id);

    int insert(SysResource record);

    int insertSelective(SysResource record);

    SysResource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysResource record);

    int updateByPrimaryKey(SysResource record);

}
