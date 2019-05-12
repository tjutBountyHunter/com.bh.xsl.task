package resource;

import vo.TaskInfoListReqVo;
import vo.TaskInfoListResVo;
import vo.TaskInfoResVo;

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

}
