package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslCollecth;
import xsl.pojo.XslCollecthExample;

import java.util.List;

public interface XslCollecthMapper {
    long countByExample(XslCollecthExample example);

    int deleteByExample(XslCollecthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslCollecth record);

    int insertSelective(XslCollecth record);

    List<XslCollecth> selectByExample(XslCollecthExample example);

    XslCollecth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslCollecth record, @Param("example") XslCollecthExample example);

    int updateByExample(@Param("record") XslCollecth record, @Param("example") XslCollecthExample example);

    int updateByPrimaryKeySelective(XslCollecth record);

    int updateByPrimaryKey(XslCollecth record);
}