package ${basePackage}.service;

import ${basePackage}.model.${model}DTO;
import ${basePackage}.repository.entity.${model};
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ${model}Service {
    ${model} create(${model}DTO ${modelCamel}DTO);
    ${model} update(long id , ${model}DTO ${modelCamel}DTO);
    ${model} getOrNull(Long id);
    ${model} getOrFail(Long id);
    Page<${model}> list(Pageable pager,String keyword);
    boolean delete(Long id );
}
