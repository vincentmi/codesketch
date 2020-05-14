package ${basePackage}.service;

import ${basePackage}.dto.${model}Dto;
import ${basePackage}.repository.${model}Repository;
import ${basePackage}.repository.entity.${model};
import com.vnzmi.commons.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;

import javax.transaction.Transactional;

@Service
public class ${model}Service {

    @Autowired
    ${model}Repository repository;

    @Transactional
    public ${model} create(${model}Dto dto)
    {
        ${model} item = new ${model}(dto);
        item = repository.save(item);
        return item;
    }

    @Transactional
    public ${model} update(long id , ${model}Dto dto)
    {
        ${model} item = getOrFail(id);
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
    public ${model} getOrNull(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public ${model} getOrFail(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND, ErrorCode.ENTITY_NOT_FOUND_MESSAGE);
        }

        ${model} item = getOrNull(id);
        if (item == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND, ErrorCode.ENTITY_NOT_FOUND_MESSAGE);
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
        return repository.findAll(specification,pager);
    }

    @Transactional
    public boolean delete(Long id ) {
        ${model} item = getOrFail(id);
        repository.delete(item);
        return true;
    }
}
