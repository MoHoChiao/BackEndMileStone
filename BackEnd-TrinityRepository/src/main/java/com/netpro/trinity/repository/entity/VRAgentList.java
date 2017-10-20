package com.netpro.trinity.repository.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name="jcsvirtualagentlist")
@IdClass(VRAgentListPKs.class)
public class VRAgentList {
	@Id
	private String virtualagentuid;
	@Id
	private String agentuid;
	@Column(nullable=false)
	private String activate;
	@Column
	private String description;
	@Column
	private Integer seq;
	@Column
  	@Temporal(TemporalType.TIMESTAMP)
  	private Date lastupdatetime;
	@ManyToOne
	@JoinColumn(name="virtualagentuid", insertable = false, updatable = false)
	@JsonBackReference	//Avoid one-to-many relationship, JSON infinite recursive.
	private VRAgent vrAgent;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="agentuid", insertable = false, updatable = false)
	@JsonManagedReference	//Avoid one-to-one relationship, JSON infinite recursive.
  	private JCSAgent jcsAgent;

	public String getVirtualagentuid() {
		return virtualagentuid;
	}

	public void setVirtualagentuid(String virtualagentuid) {
		this.virtualagentuid = virtualagentuid;
	}

	public String getAgentuid() {
		return agentuid;
	}

	public void setAgentuid(String agentuid) {
		this.agentuid = agentuid;
	}

	public String getActivate() {
		return activate;
	}

	public void setActivate(String activate) {
		this.activate = activate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastupdatetime() {
		return lastupdatetime;
	}
	
	public void setLastupdatetime(Date lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	public VRAgent getVrAgent() {
		return vrAgent;
	}

	public void setVrAgent(VRAgent vrAgent) {
		this.vrAgent = vrAgent;
	}
	
	public JCSAgent getJcsAgent() {
		return jcsAgent;
	}

	public void setJcsAgent(JCSAgent jcsAgent) {
		this.jcsAgent = jcsAgent;
	}
}