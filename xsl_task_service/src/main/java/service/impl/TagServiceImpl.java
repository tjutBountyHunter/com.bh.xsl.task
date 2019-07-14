package service.impl;

import com.google.gson.Gson;
import com.xsl.task.vo.TagReqVo;
import com.xsl.task.vo.TagResVo;
import com.xsl.task.vo.TagVo;
import mapper.XslTagMapper;
import mapper.XslTaskTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import service.TagService;
import util.GsonSingle;
import util.JedisClientUtil;
import util.ListUtil;
import xsl.pojo.XslTag;
import xsl.pojo.XslTagExample;
import xsl.pojo.XslTaskTagExample;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {

	private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

	@Autowired
	private XslTaskTagMapper xslTaskTagMapper;
	@Autowired
	private XslTagMapper xslTagMapper;

	@Value("${TASK_TAG_INFO}")
	private String TASK_TAG_INFO;


	@Override
	public TagResVo createTags(TagReqVo tagReqVo) {
		String tagName = tagReqVo.getTagName();
		TagResVo tagResVo = new TagResVo();
		tagResVo.setStatus(200);
		tagResVo.setMsg("创建标签成功");
		if(StringUtils.isEmpty(tagName)){
			tagResVo.setStatus(400);
			tagResVo.setMsg("参数错误");
			return tagResVo;
		}

		try {
			XslTagExample xslTagExample = new XslTagExample();
			XslTagExample.Criteria criteria = xslTagExample.createCriteria();
			criteria.andNameEqualTo(tagName);
			List<XslTag> list = xslTagMapper.selectByExample(xslTagExample);
			if(list != null && list.size()>0){
				return tagResVo;
			}

			XslTag xslTag = new XslTag();
			xslTag.setTagid(UUID.randomUUID().toString().substring(0,6));
			xslTag.setName(tagName);
			xslTag.setCreatedate(new Date());
			xslTagMapper.insertSelective(xslTag);
			tagResVo.setTagid(xslTag.getTagid());
			return tagResVo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<TagVo> queryTag(TagReqVo tagReqVo) {
		Integer tagNum = tagReqVo.getTagNum();
		List<String> obtainedTags = tagReqVo.getObtainedTags();
		XslTagExample xslTagExample = new XslTagExample();
		XslTagExample.Criteria criteria = xslTagExample.createCriteria();

		if(obtainedTags != null && obtainedTags.size() > 0){
			criteria.andTagidNotIn(obtainedTags);
		}

		List<XslTag> list = xslTagMapper.selectByExampleLimit(xslTagExample, tagNum);
		List<TagVo> tagVos = new ArrayList<>();
		for (XslTag xslTag : list){
			TagVo tagVo = new TagVo();
			tagVo.setTagid(xslTag.getTagid());
			tagVo.setTagName(xslTag.getName());
			tagVos.add(tagVo);
		}

		logger.info("queryTag.list msg:" + GsonSingle.getGson().toJson(tagVos));
		return tagVos;
	}

	@Override
	public List<XslTag> getTaskTags(String taskId) {
		Gson gson = GsonSingle.getGson();
		if(StringUtils.isEmpty(taskId)){
			return new ArrayList<>();
		}

		String schoolInfo = JedisClientUtil.get(TASK_TAG_INFO + ":" + taskId);

		if(!StringUtils.isEmpty(schoolInfo)){
			XslTag[] xslArray = gson.fromJson(schoolInfo, XslTag[].class);
			List<XslTag> xslTags = Arrays.asList(xslArray);
			return xslTags;
		}

		XslTaskTagExample xslTaskTagExample = new XslTaskTagExample();
		xslTaskTagExample.createCriteria().andTaskidEqualTo(taskId);
		List<String> tagIds = xslTaskTagMapper.selectTagIdByExample(xslTaskTagExample);
		XslTagExample xslTagExample = new XslTagExample();
		xslTagExample.createCriteria().andTagidIn(tagIds);
		List<XslTag> xslTags = xslTagMapper.selectByExample(xslTagExample);
		if(ListUtil.isNotEmpty(xslTags)){
			JedisClientUtil.setEx(TASK_TAG_INFO + ":" + taskId, gson.toJson(xslTags), 300);
		}

		return xslTags;
	}
}
