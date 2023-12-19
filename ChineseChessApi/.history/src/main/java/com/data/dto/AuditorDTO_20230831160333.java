package com.data.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.common.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@MappedSuperclass
public class AuditorDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date createdDate;

    private Long createdByUserId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date lastModifiedDate;

    private Long lastModifiedByUserId;

}