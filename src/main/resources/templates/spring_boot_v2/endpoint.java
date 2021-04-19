package ${basePackage}.controller;


import ${basePackage}.model.${model}DTO;
import ${basePackage}.model.${model}VO;
import ${basePackage}.service.${model}Service;
import ${basePackage}.repository.entity.${model};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import cn.zhilingapp.starter.api.common.PageResponse;
import javax.validation.Valid;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("${TAG_DOLLAR}{app.prefix.api:/api}/${modelMidLine}")
@Api(tags="${tableInfo.comment}")
public class ${model}Controller {

    ${model}Service ${modelCamel}Service ;

    @Autowired
    public void set${model}Service(${model}Service ${modelCamel}Service)
    {
        this.${modelCamel}Service = ${modelCamel}Service;
    }

    @GetMapping
    @ApiOperation("${tableInfo.comment}查询")
    public PageResponse list(Pageable pager,String keyword) {
        Page<${model}> pageData = ${modelCamel}Service.list(pager,keyword);
        return PageResponse.create(pageData ,e -> new ${model}VO((${model}) e));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取${tableInfo.comment}详情")
    public ${model}VO detail(@PathVariable("id")  long  id )
    {
        return new ${model}VO(${modelCamel}Service.getOrFail(id));
    }

    @PostMapping
    @ApiOperation("创建${tableInfo.comment}")
    public ${model}VO create(@RequestBody @Valid ${model}DTO ${modelCamel}DTO)
    {
        ${model} item1 = ${modelCamel}Service.create(${modelCamel}DTO);

        return new ${model}VO(item1);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改${tableInfo.comment}")
    public ${model}VO update(@PathVariable("id")  long id , @RequestBody @Valid  ${model}DTO ${modelCamel}DTO)
    {
        ${model} item1 = ${modelCamel}Service.update(id,${modelCamel}DTO);

        return new ${model}VO(item1);
    }


    @DeleteMapping("/{id}")
    @ApiOperation("删除${tableInfo.comment}")
    public boolean delete(@PathVariable("id")  long id )
    {
        return ${modelCamel}Service.delete(id);
    }
}
