package service.impl;

import com.github.pagehelper.PageHelper;

import mapper.XslOrderMapper;
import mapper.XslTaskMapper;
import mapper.XslUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.XslOrderService;
import vo.OderVo;
import vo.PageObject;
import vo.OrderReqVo;
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
            XslUserExample.Criteria hunterCriteria=hunterExample.createCriteria();
            XslUserExample.Criteria masterCriteria=masterExample.createCriteria();
            XslTaskExample.Criteria taskCriteria=taskExample.createCriteria();
            XslOrderExample.Criteria orderCriteria=OrderExample.createCriteria();
            PageHelper.startPage(orderReqVo.getPage(), orderReqVo.getRows());
            List<OderVo> oderResVoList=new LinkedList<>();
          List<XslOrder> orderList = xslOrderMapper.selectByExample(OrderExample);
                  for(XslOrder tmp:orderList){
                      OderVo oderResVo=new OderVo();
                      //查询任务名
                      taskCriteria.andTaskidEqualTo(tmp.getTaskid());
                      List<XslTask> taskList=xslTaskMapper.selectByExample(taskExample);
                      if(taskList.size()!=0)
                      oderResVo.setTaskName(taskList.get(0).getTasktitle());
                      //查询hunter名
                      hunterCriteria.andHunteridEqualTo(tmp.getReceiveid());
                      List<XslUser> userList=xslUserMapper.selectByExample(hunterExample);
                      if(userList.size()!=0)
                          oderResVo.setReceiveName(userList.get(0).getName());
                      //查询master名
                      masterCriteria.andMasteridEqualTo(tmp.getSendid());
                      userList=xslUserMapper.selectByExample(masterExample);
                      if(userList.size()!=0)
                          oderResVo.setSendName(userList.get(0).getName());
                      oderResVo.setMoney(tmp.getMoney());
                      oderResVo.setState(tmp.getState());
                      oderResVo.setOderId(tmp.getOrderid());
                      oderResVo.setStartDate(tmp.getStartdate());
                      oderResVo.setEndDate(tmp.getEnddate());
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
