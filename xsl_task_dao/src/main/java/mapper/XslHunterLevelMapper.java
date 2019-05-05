package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslHunterLevel;
import xsl.pojo.XslHunterLevelExample;

import java.util.List;

public interface XslHunterLevelMapper {
    long countByExample(XslHunterLevelExample example);

    int deleteByExample(XslHunterLevelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslHunterLevel record);

    int insertSelective(XslHunterLevel record);

    List<XslHunterLevel> selectByExample(XslHunterLevelExample example);

    XslHunterLevel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslHunterLevel record, @Param("example") XslHunterLevelExample example);

    int updateByExample(@Param("record") XslHunterLevel record, @Param("example") XslHunterLevelExample example);

    int updateByPrimaryKeySelective(XslHunterLevel record);

    int updateByPrimaryKey(XslHunterLevel record);
}