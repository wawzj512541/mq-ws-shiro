package com.example.shiro.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 * 
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@Data
@TableName("web_role")
public class WebRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long roleId;
	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	private String roleName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 部门ID
	 */
	@TableField(exist=false)
	private Long deptId;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 最后操作时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date updateTime;
	/**
	 * 最后操作人
	 */
	private String modifyBy;
	/**
	 * 状态 0正常；1禁用
	 */
	private Integer status;
	@TableField(exist=false)
	private List<Long> menuIdList;
	@TableField(exist=false)
	private List<Long> selectMenuIdList;
}
