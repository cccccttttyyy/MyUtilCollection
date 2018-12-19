package com.tsrain.springBootUtils.entity;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author cuitianyu
 * @since 2018-12-19
 */
@TableName("dsp_model")
public class DspModel extends Model<DspModel> {

    private static final long serialVersionUID = 1L;
	private int id;
	private String modelName;
	private String tableName;
	private Date createTime;
	private Date updateTime;
	private String status;


	public String getModelName() {
		return modelName;
	}

	public DspModel setModelName(String modelName) {
		this.modelName = modelName;
		return this;
	}

	public String getTableName() {
		return tableName;
	}

	public DspModel setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public DspModel setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DspModel setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public DspModel setStatus(String status) {
		this.status = status;
		return this;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DspModel{" +
			"modelName=" + modelName +
			", tableName=" + tableName +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", status=" + status +
			"}";
	}
}
