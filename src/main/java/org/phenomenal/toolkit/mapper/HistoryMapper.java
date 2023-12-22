package org.phenomenal.toolkit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.phenomenal.toolkit.entities.History;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface HistoryMapper extends BaseMapper<History> {
    @Select("SELECT * from history where uid = #{uid}")
    List<History> getHistoryByUid(long uid);
}
