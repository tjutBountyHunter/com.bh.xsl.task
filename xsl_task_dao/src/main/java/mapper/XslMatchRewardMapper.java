package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslMatchReward;
import xsl.pojo.XslMatchRewardExample;

import java.util.List;

public interface XslMatchRewardMapper {
    long countByExample(XslMatchRewardExample example);

    int deleteByExample(XslMatchRewardExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslMatchReward record);

    int insertSelective(XslMatchReward record);

    List<XslMatchReward> selectByExample(XslMatchRewardExample example);

    XslMatchReward selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslMatchReward record, @Param("example") XslMatchRewardExample example);

    int updateByExample(@Param("record") XslMatchReward record, @Param("example") XslMatchRewardExample example);

    int updateByPrimaryKeySelective(XslMatchReward record);

    int updateByPrimaryKey(XslMatchReward record);
}