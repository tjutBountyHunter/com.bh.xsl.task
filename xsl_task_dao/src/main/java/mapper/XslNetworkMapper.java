package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslNetwork;
import xsl.pojo.XslNetworkExample;

import java.util.List;

public interface XslNetworkMapper {
    long countByExample(XslNetworkExample example);

    int deleteByExample(XslNetworkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslNetwork record);

    int insertSelective(XslNetwork record);

    List<XslNetwork> selectByExample(XslNetworkExample example);

    List<XslNetwork> selectLimit();

    XslNetwork selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslNetwork record, @Param("example") XslNetworkExample example);

    int updateByExample(@Param("record") XslNetwork record, @Param("example") XslNetworkExample example);

    int updateByPrimaryKeySelective(XslNetwork record);

    int updateByPrimaryKey(XslNetwork record);
}