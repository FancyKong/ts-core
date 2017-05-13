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
    /*

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
