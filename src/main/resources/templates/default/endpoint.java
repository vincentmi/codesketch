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

    @RequestMapping(value = "",method = RequestMethod.GET)
    public PageResponse list(Pageable pager) {
        Page<${model}> page = ${modelCamel}Service.list(pager);
        Collection<${model}Dto> data = page.getContent().stream().map(
                item -> new ${model}Dto(item)
        ).collect(Collectors.toList());

        return PageResponse.pack(page ,data);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ${model}Dto detail(@PathVariable  long  id )
    {
        return new ${model}Dto(${modelCamel}Service.getById(id));
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ${model}Dto create(@RequestBody @Valid ${model}Dto item)
    {
        ${model} item1 = ${modelCamel}Service.create(item);

        return new ${model}Dto(item1);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ${model}Dto update(@PathVariable  long id , @RequestBody @Valid  ${model}Dto item)
            throws BusinessException
    {
        Application item1 = ${modelCamel}Service.update(id,item);

        return new ${model}Dto(item1);
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public boolean delete(@PathVariable  long id )
            throws BusinessException
    {
        return ${modelCamel}Service.delete(id);
    }
}
