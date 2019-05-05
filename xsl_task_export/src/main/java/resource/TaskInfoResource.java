package resource;

import vo.TaskInfoListReqVo;
import vo.XslResult;

public interface TaskInfoResource {
	/**
	 * 初始化任务大厅数据
	 * @return
	 */
	XslResult initTaskInfo(TaskInfoListReqVo taskInfoListReqVo);


	/**
	 * 刷新任务大厅数据
	 * @return
	 */
	XslResult reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo);

	/**
	 * 任务详情
	 * @return
	 */
	XslResult taskInfo(String taskId);

	/**
	 * 根据任务id取得任务
	 * @return
	 */



}
