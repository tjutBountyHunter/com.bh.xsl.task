package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslSchoolinfo;
import xsl.pojo.XslSchoolinfoExample;

import java.util.List;

public interface XslSchoolinfoMapper {
    long countByExample(XslSchoolinfoExample example);

    int deleteByExample(XslSchoolinfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslSchoolinfo record);

    int insertSelective(XslSchoolinfo record);

    List<XslSchoolinfo> selectByExample(XslSchoolinfoExample example);

    XslSchoolinfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslSchoolinfo record, @Param("example") XslSchoolinfoExample example);

    int updateByExample(@Param("record") XslSchoolinfo record, @Param("example") XslSchoolinfoExample example);

    int updateByPrimaryKeySelective(XslSchoolinfo record);

    int updateByPrimaryKey(XslSchoolinfo record);
}