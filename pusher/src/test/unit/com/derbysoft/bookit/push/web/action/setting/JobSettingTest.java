package com.derbysoft.bookit.push.web.action.setting;

import com.derbysoft.bookit.push.web.action.setting.support.JobSetting;
import org.junit.Assert;
import org.junit.Test;

public class JobSettingTest {


    @Test
    public void testBuildStandardCronExpress() throws Exception {
        Assert.assertTrue(new JobSetting("1", "2", "3", "4").buildStandardCronExpress().equals("0 1/2 3/4 * * ?"));
        Assert.assertTrue(new JobSetting("5", "", "", "").buildStandardCronExpress().equals("0 5 * * * ?"));
        Assert.assertTrue(new JobSetting("5", "0", "", "").buildStandardCronExpress().equals("0 5 * * * ?"));
        Assert.assertTrue(new JobSetting("5", "*", "", "").buildStandardCronExpress().equals("0 5 * * * ?"));
        Assert.assertTrue(new JobSetting("", "1", "", "").buildStandardCronExpress().equals("0 0/1 * * * ?"));
        Assert.assertTrue(new JobSetting("", "", "1", "").buildStandardCronExpress().equals("0 * 1 * * ?"));
        Assert.assertTrue(new JobSetting("", "", "2", "1").buildStandardCronExpress().equals("0 * 2/1 * * ?"));
        Assert.assertTrue(new JobSetting("","","","").buildStandardCronExpress().equals("0 * * * * ?"));
    }

    @Test
    public void testFromStandardCronExpress() throws Exception {
        Assert.assertTrue(JobSetting.fromStandardCronExpress("10 * * * * ?").buildStandardCronExpress().equals("0 * * * * ?"));
        Assert.assertTrue(JobSetting.fromStandardCronExpress("10 2/1 * * * ?").buildStandardCronExpress().equals("0 2/1 * * * ?"));
        Assert.assertTrue(JobSetting.fromStandardCronExpress("5 1/5 * * * ?").buildStandardCronExpress().equals("0 1/5 * * * ?"));
        Assert.assertTrue(JobSetting.fromStandardCronExpress("0 0 2 * * ?").buildStandardCronExpress().equals("0 0 2 * * ?"));
        Assert.assertTrue(JobSetting.fromStandardCronExpress("5 0 2/5 * * ?").buildStandardCronExpress().equals("0 0 2/5 * * ?"));
    }
}
