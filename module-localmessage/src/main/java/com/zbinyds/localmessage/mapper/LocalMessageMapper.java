package com.zbinyds.localmessage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbinyds.localmessage.model.po.LocalMessagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author zbinyds@126.com
 * @since 2025-04-04 19:56
 */

@Mapper
public interface LocalMessageMapper extends BaseMapper<LocalMessagePO> {
    @Select("select * from local_message where status in('INIT', 'RETRY') and next_retry_time < #{now} and create_time < #{afterTime} ")
    List<LocalMessagePO> getWaitRetryRecords(@Param(value = "now") Date now, @Param(value = "afterTime") Date afterTime);
}
