package ${basePackage}.endpoint;

import ${basePackage}.dto.ApplicationDto;
import ${basePackage}.repository.entity.Application;
import ${basePackage}.service.ApplicationService;
import io.philo.ops.util.BusinessException;
import io.philo.ops.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${TAG_DOLLAR}{application.url.prefix.v1}/${modelMidLine}")
public class ${model}Endpoint {

    @Autowired
    ${model}Service ${modelCamel}Service ;

    @GetMapping
    public PageResponse list(Pageable pager,String keyword) {
        Page<${model}> pageData = ${modelCamel}Service.list(pager,keyword);
        return PageResponse.create(pageData ,e -> new ${model}Dto((${model}) e));
    }

    @GetMapping("/{id}")
    public ${model}Dto detail(@PathVariable("id")  long  id )
    {
        return new ${model}Dto(${modelCamel}Service.getOrFail(id));
    }

    @PostMapping
    public ${model}Dto create(@RequestBody @Valid ${model}Dto item)
    {
        ${model} item1 = ${modelCamel}Service.create(item);

        return new ${model}Dto(item1);
    }

    @PutMapping("/{id}")
    public ${model}Dto update(@PathVariable("id")  long id , @RequestBody @Valid  ${model}Dto item)
    {
        ${model} item1 = ${modelCamel}Service.update(id,item);

        return new ${model}Dto(item1);
    }


    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id")  long id )
            throws BusinessException
    {
        return ${modelCamel}Service.delete(id);
    }
}
