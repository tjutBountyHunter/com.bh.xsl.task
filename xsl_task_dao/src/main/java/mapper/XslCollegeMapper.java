package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslCollege;
import xsl.pojo.XslCollegeExample;

import java.util.List;

public interface XslCollegeMapper {
    long countByExample(XslCollegeExample example);

    int deleteByExample(XslCollegeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslCollege record);

    int insertSelective(XslCollege record);

    List<XslCollege> selectByExample(XslCollegeExample example);

    XslCollege selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslCollege record, @Param("example") XslCollegeExample example);

    int updateByExample(@Param("record") XslCollege record, @Param("example") XslCollegeExample example);

    int updateByPrimaryKeySelective(XslCollege record);

    int updateByPrimaryKey(XslCollege record);
}