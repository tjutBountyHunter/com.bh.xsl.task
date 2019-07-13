package service;

import com.xsl.task.vo.TagReqVo;
import com.xsl.task.vo.TagResVo;
import com.xsl.task.vo.TagVo;
import xsl.pojo.XslTag;

import java.util.List;

public interface TagService {

	/**
	 * 创建标签
	 * @param tagReqVo
	 * @return
	 */
	TagResVo createTags(TagReqVo tagReqVo);

	/**
	 * 查询标签
	 * @param tagReqVo
	 * @return
	 */
	List<TagVo> queryTag(TagReqVo tagReqVo);

	/**
	 * 通过任务Id查询任务标签
	 * @param taskId
	 * @return
	 */
	List<XslTag> getTaskTags(String taskId);

}
