package it.barusu.springboot.cache.tests;

import it.barusu.springboot.cache.TestApp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApp.class, OopsCacheConfig.class}, properties = "application-oops.yml")
public class OopsServiceTests {

    @Rule
    public OutputCaptureRule outputCaptureRule = new OutputCaptureRule();

    @Autowired
    private OopsService oopsService;

    private RedisServer redisServer;

    @Before
    public void before() {
        redisServer = RedisServer.builder()
                // fix "Can't start redis server. Check logs for details." Exception. reduce maxheap
                // if it doesn't work, you can kill redis-server-2.8.x.exe process.
                .setting("maxheap 51200000")
                .build();
        redisServer.start();
    }

    @After
    public void after() {
        redisServer.stop();
    }

    @Test
    public void testOops() throws IOException {
        String name = "jessie";
        String result = "oops! " + name;

        // #1 first
        String oops = oopsService.oops(name);
        assertThat(oops, is(result));
        assertThat(outputCaptureRule.toString(), containsString("oops..."));

        log.info("append cut-off rule.");

        String oopsAgain = oopsService.oops(name);
        assertThat(oopsAgain, is(result));
        List<String> outputs = IOUtils.readLines(IOUtils.toInputStream(outputCaptureRule.toString(),
                StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        assertThat(outputs.get(outputs.size() - 1), not(containsString("oops...")));


    }
}
