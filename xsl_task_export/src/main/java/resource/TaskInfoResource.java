package resource;

import vo.TaskInfoListReqVo;
import vo.TaskInfoListResVo;
import vo.TaskInfoResVo;

import java.math.BigDecimal;

public interface TaskInfoResource {
	/**
	 * 初始化任务大厅数据
	 * @return
	 */
	TaskInfoListResVo initTaskInfo(TaskInfoListReqVo taskInfoListReqVo);


	/**
	 * 刷新任务大厅数据
	 * @return
	 */
	TaskInfoListResVo reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo);

	/**
	 * 任务详情
	 * @return
	 */
	TaskInfoResVo taskInfo(String taskId);

	/**
	 * 所有任务数
	 * @return int
	 */
	int totalTask();
	/**
	 * 所有金额
	 * @return int
	 */
	BigDecimal totalMoney();
	/**
	 * 所有完成任务数
	 * @return int
	 */
	int totalDoneTask();

}
