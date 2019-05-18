import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.CancelTask;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-*.xml" })
public class CancelTaskTest {


    @Resource
    private CancelTask cancelTask;

    @Test
    public void test(){
        cancelTask.cancelTaskDDL();

    }
}
