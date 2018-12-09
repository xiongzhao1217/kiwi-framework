## 简介
kiwi-code-generator和kiwi-easy-coding模块是一个基于MyBatis Generator & tk-mybatis & freemarker的自动化代码构建项目，用于快速生成dao层、service层和controller代码，使用简单、快速、稳定，使我们摆脱那些重复劳动，专注于业务代码的编写。下面通过一个简单的使用演示，看如何基于Example在短短几十秒内上手。
## 特性
* 生成代码简洁优雅、扩展性强、利于维护
* 常用基础方法抽象封装
* 统一响应结果封装
* 集成MyBatis通用Mapper插件，实现单表业务零SQL
* 集成PageHelper分页插件
* 编码风格统一
* 代码生成器支持业务路径配置，生成的model，mapper，service，controller等基础代码会自动放入业务路径下
* 支持自定义生成的model类中的属性类型，如数据库字段类型smallint生成domain的字段类型为Integer
* 支持自定义代码模块，对代码风格进行定制化

## 环境要求
* **spring4及以上** 基础方法的抽象封装依赖spring4提供的对泛型注入的支持，如果spring版本在4以下，service层的代码将不起作用，建议升级spring版本到4以上，我们也可以自己实现service层代码，或者通过 **[自定义模板](https://github.com/xiongzhao1217/kiwi-framework/blob/master/doc/code-generator.md#%E8%BF%9B%E9%98%B6%E5%A6%82%E4%BD%95%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A8%A1%E6%9D%BF)** 生成servive层

## 快速上手
* **引入maven依赖**

```
<dependency>
    <groupId>com.kiwiframework</groupId>
    <artifactId>kiwi-code-generator</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.kiwiframework</groupId>
    <artifactId>kiwi-easy-coding</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

code-generator负责代码生成，scope申明为test，easy-coding包负责方法抽象封装，统一响应结果封装
<br>**由于jar包发布在我自己的私服上，直接引入依赖jar包不会被下载下来，建议下载源码后打包发布到自己的私服上**
* **快速配置**
<br>一般我们会在src/test下进行代码生成操作，这样的好处是test下的代码不会被打包。
在src/test/resources目录下新建code_generator.properties文件，内容如下

```
# 需要生成代码的模块名称(项目mvc层在同一个模块中,该配置必填)
target.module.name=kiwi-sso
# 包名,默认com.kiwiframework.demo
base.package=com.kiwiboot.kiwisso

# jdbc配置,用于读取表中字段，自动生成domain和dao层代码
jdbc.url=jdbc:mysql://xx.xx.xx:3306/demo
jdbc.username=root
jdbc.password=123
jdbc.diver.class.name=com.mysql.jdbc.Driver
```
如果你的项目是常见的mvc分层结构，按照以上配置即可，下面是完整的配置，可根据需要选择配置

```
# 注释作者,默认CodeGenerator
doc.author=xiongzhao
# 项目模块前缀,默认与项目根目录相同(项目mvc层在不同的模块中，可选该配置)
project.module.prefix=kiwi-boot
# 需要生成代码的模块名称(项目mvc层在同一个模块中,该配置必填)
target.module.name=kiwi-sso
# 包名,默认com.kiwiframework.demo
base.package=com.kiwiboot.kiwisso
# 模块路径,默认无,当项目比较大，可以对项目进行业务拆分，如系统模块system，生成的代码会放在对应的模块下
biz.module.path=system

# mapper.xml文件相对dao模块下src/resources/目录的存放路径,默认mapper
base.mapper.xml.path=mybatis.mapper
# mapper接口继承的父类，如果不配置，默认使用easy-coding包中的mapper基类，该基类已经满足全部的单表查询需求
mapper.super.interface=com.xx.xxx.BaseMapper
# 控制器包路径,默认controller
base.controller=web

# jdbc配置,用于读取表中字段，自动生成domain和dao层代码
jdbc.url=jdbc:mysql://xx.xx.xx:3306/demo
jdbc.username=root
jdbc.password=123
jdbc.diver.class.name=com.mysql.jdbc.Driver

```

* **代码生成**
<br>在src/test/java下的任意目录新建代码生成入口类，如CodeGeneratorTest.java，引入工具包中的CodeGenerator类，运行main方法，生成的代码会自动放入相应的位置。CodeGenerator提供了dao、servive和controller层分别生成和全部生成的静态方法，使用时可以根据需要选择。CodeGenerator.genCode方法默认会将数据库中的表名按照驼峰法转换后生成对应的java文件名称，如果我们有自定义的需求，可以通过调用CodeGenerator.genCodeByCustomModelName方法来指定文件名称。

```
import com.kiwiframework.generator.CodeGenerator;

public class CodeGeneratorTool {

    public static void main(String[] args) {
        // 转驼峰法生成domain,dao,service和controller
        CodeGenerator.genDefaultCode("system_config");
        // 自定义domain名称生成domain,dao,service和controller
        CodeGenerator.genCode("system_config", "Config");
        // 只生成dao层
        CodeGenerator.genModelAndMapper("system_config", "Config");
        // 只生成service层
        CodeGenerator.genService("system_config", "Config");
        // 只生成controller层
        CodeGenerator.genController("system_config", "Config");
    }

}
```

经过以上三步我们完成了代码的生成
* 生成代码结构
![image](https://github.com/xiongzhao1217/markdown-photos/blob/master/generator_examp1.jpg?raw=true)
* service类
![image](https://github.com/xiongzhao1217/markdown-photos/blob/master/generator_examp2.png?raw=true)
![image](https://github.com/xiongzhao1217/markdown-photos/blob/master/generator_examp3.png?raw=true)
* service类继承的基类已经实现了单表操作常用的方法，下面的方法可以直接使用，不用再写sql
![image](https://github.com/xiongzhao1217/markdown-photos/blob/master/generator_examp4.png?raw=true)

## 项目中使用
* 通用Mapper依赖第三方包tk-mybatis,数据源的mapper扫描配置类需要由org.mybatis.spring.mapper.MapperScannerConfigurer改为tk.mybatis.spring.mapper.MapperScannerConfigurer
```
<bean  id="mapperScannerConfigurer"  class="tk.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.jd.demo.dao"></property>
    <property name="sqlSessionTemplateBeanName" value="sqlSession"></property>
</bean>
```
* sprintboot项目可以在应用启动类上使用@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.kiwiboot.kiwisso.dao")注解代替，详情可参考[【tk-mybatis官方文档】](https://github.com/abel533/Mapper)

## 如何自定义数据库字段类型对应的java类型
* 有时我们在数据库中定义字段的类型，某些情况下，为了少占用空间，我们选择使用SMALLINT作为字段类型，而mybatis-generator默认将该类型映射为java的Short，显然这种情况下如果映射为Integer在代码中操作更方便，类似这样的场景我们可以通过配置来覆盖默认的映射关系

```
## 高级配置
# 配置数据库类型与java类型的映射关系，生成器会根据该配置进行domain对象的生成，默认按照mybatis官方进行映射
# key为数据库中的字段类型，value为java字段类型
db.java.type.map={"SMALLINT": "java.lang.Integer", "DECIMAL": "java.lang.Double"}
```

## 【进阶】如何自定义模板
* 有时我们希望自己封装统一的api响应结果，这时候生成器默认的controller模板可能不适合我们，我们可以在src/test/resources/template目录下建立模板文件，生成器在执行时会优先读取我们自定义的模板，当自定义模板不存在时会使用生成器内部定义好的默认模板进行代码生成。像这样的场景还包括对servive层继承的抽象类的扩展，我们可以自己编写抽象类，通过自定义模板让servive层继承我们自己的抽象类。
* 在src/test/resources/template目录下新建模板文件，支持自定义的模板包括控制器模板controller.ftl、serive模板service.ftl、service实现类模板service-impl.ftl，建立与模板名称一致的文件即可相应覆盖
![image](https://github.com/xiongzhao1217/markdown-photos/blob/master/generator_examp5.png?raw=true)
* 以控制器模板controller.ftl为例，很多时候我们只需要复制一份生成器默认的模板，然后进行小的改动就可实现我们自定义的需求，例如我们通过替换模板中默认引用的统一api响应类ApiResult和ResultGenerator实现自己封装统一的api响应结果的需求。模板中必要的变量已经定义好，通过freemarker语法引入变量的值。

```
package ${basePackage}${baseController}${modulePath};
import com.kiwiframework.easycoding.api.ApiResult;
import com.kiwiframework.easycoding.api.ResultGenerator;
import ${basePackage}.domain${modulePath}.${modelNameUpperCamel};
import ${basePackage}.service${modulePath}.${modelNameUpperCamel}Service;
import com.kiwiframework.easycoding.PageBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * ${modelNameUpperCamel}Controller created on ${date}.
 * @author ${author}.
 */
@Controller
@RequestMapping("/${modulePath ? substring(1)}/${modelNameLowerCamel}")
public class ${modelNameUpperCamel}Controller {
    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.insertSelective(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestParam Integer id) {
        ${modelNameLowerCamel}Service.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.updateSelective(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public Result detail(@RequestParam Integer id) {
        ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.selectById(id);
        return ResultGenerator.genSuccessResult(${modelNameLowerCamel});
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Result list(PageBean pageBean, ${modelNameUpperCamel} query) {
        PageHelper.startPage(pageBean).setOrderBy(pageBean.getOrderBy());
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.find(query);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}

```

* 变量详解

```
basePackage: code_generator.properties文件中定义的base.package
baseController: code_generator.properties文件中定义的base.controller
modulePath: code_generator.properties文件中定义的biz.module.path
modelNameUpperCamel: domain名称首字母大写
modelNameLowerCamel: domain名称首字母小写
date: 当前日期
author: code_generator.properties文件中定义的author
```
