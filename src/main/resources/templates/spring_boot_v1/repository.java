
package ${basePackage}.repository;

import ${basePackage}.repository.entity.${model};
import com.vnzmi.commons.data.PhiloRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${model}Repository extends PhiloRepository<${model},Long> {
}
