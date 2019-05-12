package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslManagerRole;
import xsl.pojo.XslManagerRoleExample;

import java.util.List;

public interface XslManagerRoleMapper {
    long countByExample(XslManagerRoleExample example);

    int deleteByExample(XslManagerRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslManagerRole record);

    int insertSelective(XslManagerRole record);

    List<XslManagerRole> selectByExample(XslManagerRoleExample example);

    XslManagerRole selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslManagerRole record, @Param("example") XslManagerRoleExample example);

    int updateByExample(@Param("record") XslManagerRole record, @Param("example") XslManagerRoleExample example);

    int updateByPrimaryKeySelective(XslManagerRole record);

    int updateByPrimaryKey(XslManagerRole record);
}