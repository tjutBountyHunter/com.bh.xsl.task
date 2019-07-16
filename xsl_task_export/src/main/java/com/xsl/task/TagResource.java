package com.xsl.task;

import com.xsl.task.vo.TagReqVo;
import com.xsl.task.vo.TagResVo;
import com.xsl.task.vo.TagVo;

import java.util.List;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/31 22:28
 */
public interface TagResource {

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



}
