package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslManager;
import xsl.pojo.XslManagerExample;

import java.util.List;

public interface XslManagerMapper {
    long countByExample(XslManagerExample example);

    int deleteByExample(XslManagerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslManager record);

    int insertSelective(XslManager record);

    List<XslManager> selectByExample(XslManagerExample example);

    XslManager selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslManager record, @Param("example") XslManagerExample example);

    int updateByExample(@Param("record") XslManager record, @Param("example") XslManagerExample example);

    int updateByPrimaryKeySelective(XslManager record);

    int updateByPrimaryKey(XslManager record);
}