package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslDatetime;
import xsl.pojo.XslDatetimeExample;

import java.util.List;

public interface XslDatetimeMapper {
    long countByExample(XslDatetimeExample example);

    int deleteByExample(XslDatetimeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslDatetime record);

    int insertSelective(XslDatetime record);

    List<XslDatetime> selectByExample(XslDatetimeExample example);

    XslDatetime selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslDatetime record, @Param("example") XslDatetimeExample example);

    int updateByExample(@Param("record") XslDatetime record, @Param("example") XslDatetimeExample example);

    int updateByPrimaryKeySelective(XslDatetime record);

    int updateByPrimaryKey(XslDatetime record);
}