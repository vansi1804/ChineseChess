package com.data.dto;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.common.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class AuditingDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
	protected Date createdDate;
	
	protected Long createdByUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
	protected Date lastModifiedDate;

	protected Long lastModifiedByUserId;

}