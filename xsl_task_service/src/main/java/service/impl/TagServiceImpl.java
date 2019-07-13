package service.impl;

import com.xsl.task.vo.TagReqVo;
import com.xsl.task.vo.TagResVo;
import com.xsl.task.vo.TagVo;
import org.springframework.stereotype.Service;
import service.TagService;
import xsl.pojo.XslTag;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

	@Override
	public TagResVo createTags(TagReqVo tagReqVo) {
		return null;
	}

	@Override
	public List<TagVo> queryTag(TagReqVo tagReqVo) {
		return null;
	}

	@Override
	public List<XslTag> getTaskTags(String taskId) {
		return null;
	}
}
