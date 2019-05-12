package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslMaster;
import xsl.pojo.XslMasterExample;

import java.util.List;

public interface XslMasterMapper {
    long countByExample(XslMasterExample example);

    int deleteByExample(XslMasterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslMaster record);

    int insertSelective(XslMaster record);

    List<XslMaster> selectByExample(XslMasterExample example);

    XslMaster selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslMaster record, @Param("example") XslMasterExample example);

    int updateByExample(@Param("record") XslMaster record, @Param("example") XslMasterExample example);

    int updateByPrimaryKeySelective(XslMaster record);

    int updateByPrimaryKey(XslMaster record);

    int updateByMasterIdSelective(XslMaster record);
}