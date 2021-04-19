package ${basePackage}.service.impl;

import ${basePackage}.model.${model}DTO;
import ${basePackage}.service.${model}Service;
import ${basePackage}.repository.${model}Repository;
import ${basePackage}.repository.entity.${model};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import cn.zhilingapp.starter.api.common.ApiException;
import cn.zhilingapp.starter.api.common.CommonErrorCode;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class ${model}ServiceImpl implements ${model}Service {

    ${model}Repository ${modelCamel}Repository;

    @Autowired
    public void set${model}Repository(${model}Repository ${modelCamel}Repository)
    {
        this.${modelCamel}Repository = ${modelCamel}Repository;
    }

    @Transactional
    public ${model} create(${model}DTO ${modelCamel}DTO)
    {
        ${model} item = new ${model}(${modelCamel}DTO);
        ${modelCamel}Repository.save(item);
        return item;
    }

    @Transactional
    public ${model} update(long id , ${model}DTO ${modelCamel}DTO)
    {
        ${model} item = getOrFail(id);
        <#list fields as field>
        <#if field.created>
        <#elseif field.updated>
        <#elseif field.primaryKey>
        <#else>
        item.${field.java.setter}(${modelCamel}DTO.${field.java.getter}());
        </#if>
        </#list>
        ${modelCamel}Repository.save(item);
        return item;
    }

    @Transactional
    public ${model} getOrNull(Long id) {
        return ${modelCamel}Repository.findById(id).orElse(null);
    }

    @Transactional
    public ${model} getOrFail(Long id) {
        if (id == null) {
            throw new ApiException(CommonErrorCode.NOT_FOUND);
        }

        ${model} item = getOrNull(id);
        if (item == null) {
            throw new ApiException(CommonErrorCode.NOT_FOUND);
        }
        return item;
    }

    @Transactional
    public Page<${model}> list(Pageable pager,String keyword) {
        Specification<${model}> specification = (Specification<${model}>) (root, criteriaQuery, criteriaBuilder)
                -> {

            ArrayList<Predicate> topPredicates = new ArrayList<>();
            if(!StringUtils.isEmpty(keyword)){
                ArrayList<Predicate> predicates = new ArrayList<>();
                String quotedKeyword = "%"+keyword+"%";
                <#list fields as field>
                <#if field.java.type == "String">
                predicates.add(criteriaBuilder.like(root.get("${field.nameLine}") ,quotedKeyword));
                </#if>
                </#list>
                if(predicates.size() > 0)
                {
                    topPredicates.add(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
                }
            }
            if (topPredicates.size() > 0) {
                return criteriaBuilder.and(topPredicates.toArray(new Predicate[topPredicates.size()]));
            } else {
                return null;
            }
        };
        return ${modelCamel}Repository.findAll(specification,pager);
    }

    @Transactional
    public boolean delete(Long id ) {
        ${model} item = getOrFail(id);
        ${modelCamel}Repository.delete(item);
        return true;
    }
}
