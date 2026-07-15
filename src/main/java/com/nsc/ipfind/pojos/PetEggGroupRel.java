package com.nsc.ipfind.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 宠物-蛋组多对多关联表(无外键)
 * @TableName pet_egg_group_rel
 */
@TableName(value ="pet_egg_group_rel")
@Data
public class PetEggGroupRel {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("pet_id")
    private Integer petId;

    @TableField("egg_group_id")
    private Integer eggGroupId;

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
        PetEggGroupRel other = (PetEggGroupRel) that;
        return (this.getPetId() == null ? other.getPetId() == null : this.getPetId().equals(other.getPetId()))
            && (this.getEggGroupId() == null ? other.getEggGroupId() == null : this.getEggGroupId().equals(other.getEggGroupId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPetId() == null) ? 0 : getPetId().hashCode());
        result = prime * result + ((getEggGroupId() == null) ? 0 : getEggGroupId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", petId=").append(petId);
        sb.append(", eggGroupId=").append(eggGroupId);
        sb.append("]");
        return sb.toString();
    }
}
