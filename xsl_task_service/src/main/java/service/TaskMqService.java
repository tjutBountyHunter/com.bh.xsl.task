package service;

import com.xsl.task.vo.TaskEsInfo;
import com.xsl.task.vo.UpdateTaskVo;
import vo.CreateOrderReqVo;

public interface TaskMqService {

	void updateEsTask(UpdateTaskVo updateTaskVo);

	void addEsTask(TaskEsInfo taskEsInfo);

	void createOrder(CreateOrderReqVo createOrderReqVo);

	void updateNetwork(String msg);


}
