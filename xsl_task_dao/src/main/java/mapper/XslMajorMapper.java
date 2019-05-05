package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslMajor;
import xsl.pojo.XslMajorExample;

import java.util.List;

public interface XslMajorMapper {
    long countByExample(XslMajorExample example);

    int deleteByExample(XslMajorExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslMajor record);

    int insertSelective(XslMajor record);

    List<XslMajor> selectByExample(XslMajorExample example);

    XslMajor selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslMajor record, @Param("example") XslMajorExample example);

    int updateByExample(@Param("record") XslMajor record, @Param("example") XslMajorExample example);

    int updateByPrimaryKeySelective(XslMajor record);

    int updateByPrimaryKey(XslMajor record);
}