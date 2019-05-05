package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslHistoryh;
import xsl.pojo.XslHistoryhExample;

import java.util.List;

public interface XslHistoryhMapper {
    long countByExample(XslHistoryhExample example);

    int deleteByExample(XslHistoryhExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslHistoryh record);

    int insertSelective(XslHistoryh record);

    List<XslHistoryh> selectByExample(XslHistoryhExample example);

    XslHistoryh selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslHistoryh record, @Param("example") XslHistoryhExample example);

    int updateByExample(@Param("record") XslHistoryh record, @Param("example") XslHistoryhExample example);

    int updateByPrimaryKeySelective(XslHistoryh record);

    int updateByPrimaryKey(XslHistoryh record);
}