package com.nsc.ipfind.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 宠物-属性多对多关联表(无外键)
 * @TableName pet_type_rel
 */
@TableName(value ="pet_type_rel")
@Data
public class PetTypeRel {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("pet_id")
    private Integer petId;

    @TableField("type_id")
    private Integer typeId;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PetTypeRel other = (PetTypeRel) that;
        return (this.getPetId() == null ? other.getPetId() == null : this.getPetId().equals(other.getPetId()))
            && (this.getTypeId() == null ? other.getTypeId() == null : this.getTypeId().equals(other.getTypeId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPetId() == null) ? 0 : getPetId().hashCode());
        result = prime * result + ((getTypeId() == null) ? 0 : getTypeId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", petId=").append(petId);
        sb.append(", typeId=").append(typeId);
        sb.append("]");
        return sb.toString();
    }
}
