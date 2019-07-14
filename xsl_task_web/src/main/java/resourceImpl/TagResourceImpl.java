package resourceImpl;

import com.xsl.task.TagResource;
import com.xsl.task.vo.TagReqVo;
import com.xsl.task.vo.TagResVo;
import com.xsl.task.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.TagService;

import java.util.List;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/31 22:29
 */
@Controller
public class TagResourceImpl implements TagResource {

    @Autowired
    private TagService tagService;

    @Override
    public TagResVo createTags(TagReqVo tagReqVo) {
        try {
            return tagService.createTags(tagReqVo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TagVo> queryTag(TagReqVo tagReqVo) {
        try {
            return tagService.queryTag(tagReqVo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
