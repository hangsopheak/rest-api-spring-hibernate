package com.sample.repo.server;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.server.SkipNullObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes = { TestMvcConfig.class })
public abstract class AbstractWebAppTest {

    public MockMvc server;
    protected ObjectMapper mapper = null;

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractWebAppTest.class);

    @Resource
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {

        if (server !=null) {
            return;
        }
        LOG.info("====================setUp================");

        MockitoAnnotations.initMocks(this);

        mapper = new SkipNullObjectMapper();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);

        server = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @BeforeClass
    public static void setUpBeforeClass() {
       // helper.setUp();
    }

    @After
    public void tearDown() {

    }

    @AfterClass
    public static void tearDownAfterClass() {
      //  helper.tearDown();
    }

    protected ResultActions status200(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.OK.value()));
    }
    protected ResultActions status302(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.FOUND.value()));
    }

    protected ResultActions status400(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    protected ResultActions status401(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    protected ResultActions status403(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    protected ResultActions status404(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    protected ResultActions status201(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.CREATED.value()));
    }

    protected ResultActions status204(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    protected ResultActions status500(final ResultActions actions) throws Exception {
        return actions.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    protected String redirectedUrl(final ResultActions actions) throws Exception {
        return actions.andReturn().getResponse().getRedirectedUrl();
    }

    protected String header(final ResultActions actions, String key) throws Exception {
        return actions.andReturn().getResponse().getHeader(key);
    }

    protected String json(ResultActions response) {
        try {
            return response.andReturn().getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected <T> T parseResponseBody(ResultActions response, Class<T> clazz) {
        // com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try {
            final String json = json(response);
            LOG.info("=======json {} ", json);
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    protected String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
        }
        return null;
    }

    protected void addParams(MockHttpServletRequestBuilder builder, Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(obj) != null) { // avoid injecting string "null" to boolean property, for example
                    builder.param(field.getName(), String.valueOf(field.get(obj)));
                }
            } catch (Exception e) {

            }
        }
    }

}