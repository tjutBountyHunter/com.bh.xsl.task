package resourceImpl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.TaskAccountService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-*.xml" })
public class CancelTaskTest {
    @Autowired
    private TaskAccountService taskAccountService;

    @Test
    public void totalMoneyTest(){
        Assert.assertNotNull(taskAccountService.totalMoney());
    }
}
