package com.jdkcc.ts.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * API 接口配置
 * Created by Cherish on 2017/1/4.
 */
@Slf4j
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /*
    * 访问地址
    * http://127.0.0.1:8080/swagger-ui.html
    */

    /**
     * 可以定义多个组，比如本类中定义把test和demo区分开了
     * （访问页面就可以看到效果了）
     */
    /*@Bean
    public Docket testApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("test")
                .genericModelSubstitutes(DeferredResult.class)
//                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .paths(or(regex("/test/.*")))//过滤的接口
                .build()
                .apiInfo(testApiInfo());
    }

    @Bean
    public Docket demoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("demo")
                .genericModelSubstitutes(DeferredResult.class)
//              .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .paths(or(regex("/demo/.*")))//过滤的接口
                .build()
                .apiInfo(demoApiInfo());
    }

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("user")
                .genericModelSubstitutes(DeferredResult.class)
//              .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .paths(or(regex("/user/.*")))//过滤的接口
                .build()
                .apiInfo(userApiInfo());
    }

    private ApiInfo userApiInfo() {
        return new ApiInfo("USER Controller API",
                "",
                "VERSION",
                "",
                new Contact("FancyKong","www.fancykong.com","823382133@qq.com"),
                "","");
    }

    private ApiInfo testApiInfo() {
        ApiInfo apiInfo = new ApiInfo("大标题",//大标题
                "小标题   描述  详细描述 description",//小标题
                "0.1",//版本
                "terms of service Url",
                new Contact("Cherish", "www.caihongwen.cn", "785427346@qq.com"),//作者
                "The Apache License, Version 2.0",//链接显示文字
                "http://www.apache.org/licenses/LICENSE-2.0.html"//网站链接
        );

        return apiInfo;
    }

    private ApiInfo demoApiInfo() {
        ApiInfo apiInfo = new ApiInfo("大标题就大标题咯DEMO   demo",//大标题
                "小标题   描述  详细描述 description",//小标题
                "1.0",//版本
                "NO terms of service",
                new Contact("Cherish", "www.caihongwen.cn", "785427346@qq.com"),//作者
                "The Apache License, Version 2.0",//链接显示文字
                "http://www.apache.org/licenses/LICENSE-2.0.html"//网站链接
        );

        return apiInfo;
    }

    @Bean
    public Docket wxUserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("wxUser")
                .genericModelSubstitutes(DeferredResult.class)
//                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .paths(or(regex("/wx_user/.*")))//过滤的接口
                .build()
                .apiInfo(wxUserApiInfo());
    }
    private ApiInfo wxUserApiInfo() {
        return new ApiInfo("WxUserController API",
                "微信用户控制器",
                "1.0",
                "",
                new Contact("Cherish","www.caihongwen.cn","785427346@qq.com"),
                "","");
    }*/

    private static final String SWAGGER_SCAN_BASE_PACKAGE = "com.jdkcc.ts.web.controller";
    private static final String VERSION = "1.0.0";

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))//api接口包扫描路径
                .paths(PathSelectors.any())//可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger2 接口文档示例")//设置文档的标题
                .description("更多内容请关注：http://www.cafa.com")//设置文档的描述->1.Overview
                .version(VERSION)//设置文档的版本信息-> 1.1 Version information
                .contact(
                        new Contact("Cherish","www.caihongwen.cn","785427346@qq.com")
                        )//设置文档的联系方式->1.2 Contact information
                .termsOfServiceUrl("www.cafa.com")//设置文档的License信息->1.3 License information
                .build();
    }


}
