package com.nvs.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nvs.common.Default;
import lombok.Data;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.Date;

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