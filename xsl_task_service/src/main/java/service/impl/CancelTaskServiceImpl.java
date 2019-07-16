package service.impl;

import mapper.XslSchoolTaskMapper;
import mapper.XslTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.CancelTaskService;
import util.ListUtil;
import com.xsl.task.vo.ResBaseVo;
import xsl.pojo.XslTaskExample;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Service
public class CancelTaskServiceImpl implements CancelTaskService {
    private static final Logger log =  LoggerFactory.getLogger(CancelTaskServiceImpl.class);
    @Autowired
    private XslTaskMapper xslTaskMapper;
    @Autowired
    private XslSchoolTaskMapper xslSchoolTaskMapper;


    @Override
    public ResBaseVo cancelTaskDDL() {
        XslTaskExample xslTaskExample = new XslTaskExample();
        List<Byte> values = Arrays.asList(new Byte[]{1, 0});
        xslTaskExample.createCriteria().andStateIn(values).andDeadlineLessThanOrEqualTo(new Date());
        try {
            List<String> taskIds = xslTaskMapper.selectTaskIdByExample(xslTaskExample);

            if(ListUtil.isNotEmpty(taskIds)){
                for(String taskId: taskIds){
                    xslSchoolTaskMapper.cancelTaskDDL(taskId);
                    xslTaskMapper.cancelTaskDDL(taskId);
                }
            }
            return ResBaseVo.ok(taskIds);
        }catch (Exception e){
            log.error("cancelTaskDDL exception:{}", e);
            throw new RuntimeException(e);
        }
    }
}
