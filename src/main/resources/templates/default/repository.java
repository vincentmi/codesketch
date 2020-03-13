
package ${basePackage}.repository;

import ${basePackage}.repository.entity.${model};
import io.philo.ops.util.repository.PhiloRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${model}Repository extends PhiloRepository<${model},Long> {
}
