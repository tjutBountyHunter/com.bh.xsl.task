package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslToken;
import xsl.pojo.XslTokenExample;

import java.util.List;

public interface XslTokenMapper {
    long countByExample(XslTokenExample example);

    int deleteByExample(XslTokenExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslToken record);

    int insertSelective(XslToken record);

    List<XslToken> selectByExample(XslTokenExample example);

    XslToken selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslToken record, @Param("example") XslTokenExample example);

    int updateByExample(@Param("record") XslToken record, @Param("example") XslTokenExample example);

    int updateByPrimaryKeySelective(XslToken record);

    int updateByPrimaryKey(XslToken record);
}