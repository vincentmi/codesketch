package ${basePackage}.repository.entity;

import ${basePackage}.dto.UserDto;
import com.google.common.base.MoreObjects;
import javax.persistence.*;
import org.hibernate.annotations.*;
import java.util.Date;

@Entity
@Table(name = "${table}")
public class ${model} {
    private static long serialVersionUID = 1L;
    
    <#list fields as field>
    <#compress><#if field.primaryKey>@Id</#if>
    <#if field.autoIncrement>@GeneratedValue(strategy = GenerationType.IDENTITY)</#if>
    <#if field.created>@CreationTimestamp</#if>
    <#if field.updated>@UpdateTimestamp</#if></#compress>    
    private ${field.java.type} ${field.nameCamel};
    </#list>
    

    public ${model}(){}

    public ${model}(${model}Dto dtoObject)
    {
        <#list fields as field>
        ${field.java.setter}(dtoObject.${field.java.getter}());
        </#list>
    }

    <#list fields as field>
    public ${field.java.type} ${field.java.getter}() {
        return ${field.nameCamel};
    }
    public void ${field.java.setter}(${field.java.type} value) {
        this.${field.nameCamel} = value;
    }
    </#list>

    public String toString()
    {
        return MoreObjects.toStringHelper(this)
        <#list fields as field>
                .add("${field.nameCamel}",${field.java.getter}())
        </#list>
                .toString();
    }
}
