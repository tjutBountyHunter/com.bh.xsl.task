package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslMatch;
import xsl.pojo.XslMatchExample;

import java.util.List;

public interface XslMatchMapper {
    long countByExample(XslMatchExample example);

    int deleteByExample(XslMatchExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslMatch record);

    int insertSelective(XslMatch record);

    List<XslMatch> selectByExample(XslMatchExample example);

    XslMatch selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslMatch record, @Param("example") XslMatchExample example);

    int updateByExample(@Param("record") XslMatch record, @Param("example") XslMatchExample example);

    int updateByPrimaryKeySelective(XslMatch record);

    int updateByPrimaryKey(XslMatch record);
}