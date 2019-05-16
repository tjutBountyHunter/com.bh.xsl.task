package service.impl;

import com.github.pagehelper.PageHelper;

import mapper.XslOrderMapper;
import mapper.XslTaskMapper;
import mapper.XslUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.XslOrderService;
import util.DateUtils;
import vo.OderResVo;
import vo.OrderReqVo;
import vo.PageObject;
import xsl.pojo.*;
import xsl.pojo.XslOrder;

import java.util.LinkedList;
import java.util.List;

@Service
public class XslOrderServiceImpl implements XslOrderService {
    @Autowired
    private XslUserMapper xslUserMapper;
    @Autowired
    private XslTaskMapper xslTaskMapper;
    @Autowired
    private XslOrderMapper xslOrderMapper;


    @Override
    public PageObject selectOrderAll(OrderReqVo orderReqVo) {
        XslUserExample hunterExample=new XslUserExample();
        XslUserExample masterExample=new XslUserExample();
        XslTaskExample taskExample=new XslTaskExample();
        XslOrderExample OrderExample=new XslOrderExample();
        PageObject result = new PageObject();

        try {

            XslOrderExample.Criteria orderCriteria=OrderExample.createCriteria();
            PageHelper.startPage(orderReqVo.getPage(), orderReqVo.getRows());
            List<OderResVo> oderResVoList=new LinkedList<>();
          List<XslOrder> orderList = xslOrderMapper.selectByExample(OrderExample);
                  for(XslOrder tmp:orderList){
                      OderResVo oderResVo=new OderResVo();
                      //查询任务名
                      oderResVo.setTaskName(xslTaskMapper.getTaskTitleByTaskId(tmp.getTaskid()));
                      //查询hunter master名
                      oderResVo.setSendName(xslUserMapper.getNameByHunterId(tmp.getReceiveid()));
                      oderResVo.setReceiveName(xslUserMapper.getNameByMasterId(tmp.getSendid()));
                      oderResVo.setMoney(tmp.getMoney());
                      oderResVo.setState(tmp.getState());
                      oderResVo.setOderId(tmp.getOrderid());
                      oderResVo.setStartDate(DateUtils.getDateTimeToString(tmp.getStartdate()));
                      oderResVo.setEndDate(DateUtils.getDateTimeToString(tmp.getEnddate()));
                      oderResVoList.add(oderResVo);

                  }
                  result.setData(oderResVoList);
                  result.setTotal(orderList.size());


        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }
}
