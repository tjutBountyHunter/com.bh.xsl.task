package mapper;

import org.apache.ibatis.annotations.Param;
import xsl.pojo.XslInputBill;
import xsl.pojo.XslInputBillExample;

import java.util.List;

public interface XslInputBillMapper {
    long countByExample(XslInputBillExample example);

    int deleteByExample(XslInputBillExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(XslInputBill record);

    int insertSelective(XslInputBill record);

    List<XslInputBill> selectByExample(XslInputBillExample example);

    XslInputBill selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") XslInputBill record, @Param("example") XslInputBillExample example);

    int updateByExample(@Param("record") XslInputBill record, @Param("example") XslInputBillExample example);

    int updateByPrimaryKeySelective(XslInputBill record);

    int updateByPrimaryKey(XslInputBill record);
}