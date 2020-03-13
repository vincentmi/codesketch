package ${basePackage}.service;

import ${basePackage}.dto.${model}Dto;
import ${basePackage}.repository.${model}Repository;
import ${basePackage}.repository.entity.${model};
import io.philo.ops.util.BusinessException;
import io.philo.ops.util.BusinessExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ${model}Service {

    @Autowired
    ${model}Repository repository;

    @Transactional
    public ${model} create(${model}Dto dto)
    {
        ${model} item = new ${model}(dto);
        repository.save(item);
        return item;
    }

    @Transactional
    public ${model} update(long id , ${model}Dto dto)
            throws BusinessException
    {
        ${model} item = repository.findById(id).orElse(null);

        if(item == null)
        {
            throw BusinessExceptionFactory.itemNotFound();
        }
        <#list fields as field>
        <#if field.created>
        <#elseif field.updated>
        <#elseif field.primaryKey>
        <#else>
        item.${field.java.setter}(dto.${field.java.getter}());
        </#if>
        </#list>
        repository.save(item);
        return item;
    }


    @Transactional
    public Page<${model}> list(Pageable pager) {
        Page<${model}> data = repository.findAll(pager);
        return  data;
    }

    @Transactional
    public ${model} getById(Long id ) {
        return  repository.findById(id).orElse(null);
    }

    @Transactional
    public boolean delete(Long id ) {
        ${model} item = getById(id);
        if(item != null)
        {
            repository.delete(item);
            return true;
        }else{
            return false;
        }
    }
}
