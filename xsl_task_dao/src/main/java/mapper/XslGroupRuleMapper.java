package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslGroupRule;
import xsl.pojo.XslGroupRuleExample;

import java.util.List;

public interface XslGroupRuleMapper {
    long countByExample(XslGroupRuleExample example);

    int deleteByExample(XslGroupRuleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslGroupRule record);

    int insertSelective(XslGroupRule record);

    List<XslGroupRule> selectByExample(XslGroupRuleExample example);

    XslGroupRule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslGroupRule record, @Param("example") XslGroupRuleExample example);

    int updateByExample(@Param("record") XslGroupRule record, @Param("example") XslGroupRuleExample example);

    int updateByPrimaryKeySelective(XslGroupRule record);

    int updateByPrimaryKey(XslGroupRule record);
}