package ${basePackage}.repository.entity;

import ${basePackage}.model.${model}DTO;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.util.Date;
import lombok.Data;

@Entity
@Data
@Table(name = "${table}")
<#list fields as field>
<#if field.deleted>
@SQLDelete(sql="UPDATE ${table} SET ${field.nameLine} = NOW() WHERE ${primaryKey.name} = ?")
@SQLDeleteAll(sql="UPDATE ${table} SET ${field.nameLine} = NOW() WHERE ${primaryKey.name} = ?")
@Where(clause = " ${field.nameLine} IS NULL ")
</#if>
</#list>
/**
 * ${tableInfo.comment}
 **/
public class ${model} {
    private static long serialVersionUID = 1L;
    
    <#list fields as field>
    <#if field.primaryKey>@Id</#if>
    <#if field.autoIncrement>@GeneratedValue(strategy = GenerationType.IDENTITY)</#if>
    <#if field.created>@CreationTimestamp</#if>
    <#if field.updated>@UpdateTimestamp</#if>
    @Column(name="${field.nameLine}")    
    private ${field.java.typeObject} ${field.nameCamel} ${field.java.defaultExpression};
    </#list>
    

    public ${model}(){}

    public ${model}(${model}DTO ${modelCamel}DTO)
    {
        <#list fields as field>
        <#if field.primaryKey == true >
        <#continue>
        </#if>
        ${field.java.setter}(${modelCamel}DTO.${field.java.getter}());
        </#list>
    }
}
