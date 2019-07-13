package service;

import com.xsl.task.vo.TagReqVo;
import xsl.pojo.XslResult;

public interface TagService {

	XslResult createTags(TagReqVo tagReqVo);

	XslResult queryTag(TagReqVo tagReqVo);

}
