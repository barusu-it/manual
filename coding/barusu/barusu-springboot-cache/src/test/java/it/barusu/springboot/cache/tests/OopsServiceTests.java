package it.barusu.springboot.cache.tests;

import it.barusu.springboot.cache.TestApp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import javax.annotation.Resource;
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

    @Resource
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

    /**
     * cache penetration testcase
     */
    @Test
    public void testOopsAndResultIsNull() throws IOException {
        String name = "jessie";

        String oops = oopsService.oopsAndResultIsNull(name);
        assertThat(oops, nullValue());
        assertThat(outputCaptureRule.toString(), containsString("oops and result is null..."));

        log.info("append cut-off rule.");

        String oopsAgain = oopsService.oopsAndResultIsNull(name);
        assertThat(oopsAgain, nullValue());
        List<String> outputs = IOUtils.readLines(IOUtils.toInputStream(outputCaptureRule.toString(),
                StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        assertThat(outputs.get(outputs.size() - 1), not(containsString("oops and result is null...")));

        // this testcase is passed by spring-boot 2.2.2, so spring-boot has resolved cache penetration (缓存穿透)
    }

    // spring boot cache redis 默认使用 redis.ttl 统一的过期时间（不设置默认为不过期），如需要不同 ttl，则需要修改 RedisCache
    // 而针对缓存击穿的场景，使用 spring boot cache 时，
    // 建议使用扩展的二级缓存结合一级缓存永不失效（二级缓存可以减轻一级缓存的压力），低峰定期刷新缓存的解决方案，
    // 不建议使用互斥锁的方式（考虑到性能和吞吐量问题）
}
