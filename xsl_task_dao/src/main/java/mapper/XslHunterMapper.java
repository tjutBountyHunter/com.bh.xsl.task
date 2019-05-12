package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslHunter;
import xsl.pojo.XslHunterExample;

import java.util.List;

public interface XslHunterMapper {
    long countByExample(XslHunterExample example);

    int deleteByExample(XslHunterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslHunter record);

    int insertSelective(XslHunter record);

    List<XslHunter> selectByExample(XslHunterExample example);

    XslHunter selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslHunter record, @Param("example") XslHunterExample example);

    int updateByExample(@Param("record") XslHunter record, @Param("example") XslHunterExample example);

    int updateByPrimaryKeySelective(XslHunter record);

    int updateByPrimaryKey(XslHunter record);

    int updateByHunterIdSelective(XslHunter record);

    //    重写
    int selectMaxLevel();

    int selectMaxTaskAccNum();

    XslHunter selectHunterByHunterId(String hunterId);

    List<String> selectGoodHunter();
}