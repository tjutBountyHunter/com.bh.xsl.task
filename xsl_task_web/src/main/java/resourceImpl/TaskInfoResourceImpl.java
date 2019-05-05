package resourceImpl;

import org.springframework.stereotype.Controller;
import resource.TaskInfoResource;
import vo.TaskInfoListReqVo;
import vo.XslResult;

@Controller
public class TaskInfoResourceImpl implements TaskInfoResource {
	@Override
	public XslResult initTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
		return null;
	}

	@Override
	public XslResult reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
		return null;
	}

	@Override
	public XslResult taskInfo(String taskId) {
		return null;
	}
}
