package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslMasterLevel;
import xsl.pojo.XslMasterLevelExample;

import java.util.List;

public interface XslMasterLevelMapper {
    long countByExample(XslMasterLevelExample example);

    int deleteByExample(XslMasterLevelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslMasterLevel record);

    int insertSelective(XslMasterLevel record);

    List<XslMasterLevel> selectByExample(XslMasterLevelExample example);

    XslMasterLevel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslMasterLevel record, @Param("example") XslMasterLevelExample example);

    int updateByExample(@Param("record") XslMasterLevel record, @Param("example") XslMasterLevelExample example);

    int updateByPrimaryKeySelective(XslMasterLevel record);

    int updateByPrimaryKey(XslMasterLevel record);
}