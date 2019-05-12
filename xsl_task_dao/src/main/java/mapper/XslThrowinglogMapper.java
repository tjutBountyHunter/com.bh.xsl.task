package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslThrowinglog;
import xsl.pojo.XslThrowinglogExample;

import java.util.List;

public interface XslThrowinglogMapper {
    long countByExample(XslThrowinglogExample example);

    int deleteByExample(XslThrowinglogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslThrowinglog record);

    int insertSelective(XslThrowinglog record);

    List<XslThrowinglog> selectByExample(XslThrowinglogExample example);

    XslThrowinglog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslThrowinglog record, @Param("example") XslThrowinglogExample example);

    int updateByExample(@Param("record") XslThrowinglog record, @Param("example") XslThrowinglogExample example);

    int updateByPrimaryKeySelective(XslThrowinglog record);

    int updateByPrimaryKey(XslThrowinglog record);
}