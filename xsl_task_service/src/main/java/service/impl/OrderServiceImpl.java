package service.impl;

import mapper.XslOrderMapper;
import mapper.XslTaskMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.OrderService;
import util.ListUtil;
import util.UUIDUtil;
import vo.CreateOrderReqVo;
import xsl.pojo.XslOrder;
import xsl.pojo.XslTask;
import xsl.pojo.XslTaskExample;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private XslOrderMapper xslOrderMapper;
	@Autowired
	private XslTaskMapper xslTaskMapper;

	@Override
	public void createOrder(CreateOrderReqVo createOrderReqVo) {
		XslOrder xslOrder = new XslOrder();

		XslTaskExample xslTaskExample = new XslTaskExample();
		xslTaskExample.createCriteria().andTaskidEqualTo(createOrderReqVo.getTaskId());
		List<XslTask> xslTasks = xslTaskMapper.selectByExample(xslTaskExample);

		if(!ListUtil.isNotEmpty(xslTasks)){
			return;
		}

		XslTask xslTask = xslTasks.get(0);

		BeanUtils.copyProperties(xslTask, xslOrder);
		xslOrder.setOrderid(UUIDUtil.getUUID());
		xslOrder.setReceiveid(createOrderReqVo.getHunterId());
		xslOrder.setEnddate(xslTask.getDeadline());

		xslOrderMapper.insertSelective(xslOrder);

	}
}
