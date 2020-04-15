package com.maqs.springboot.sample.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity(name = "Timesheet")
@Table(name = "timesheet")
@Getter
@Setter
public class Timesheet {

    @Id
    @Column(name = "id", nullable = false)
    private UUID uuid;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "timesheet_date", nullable = false)
    private Date timesheetDate;

    @Column(name = "sent_date", nullable = false)
    private Date sentDate;

    @Column(name = "approved_date", nullable = true)
    private Date approvedDate;

    @Column(name = "approved_by", nullable = true)
    private String approvedBy;

    @Column(name = "rejected_date", nullable = true)
    private Date rejectedDate;

    @Column(name = "rejected_by", nullable = true)
    private String rejectedBy;

    @Column(name = "rejected_reason", nullable = true)
    private String rejectedReason;

    @Column(name = "exception_date", nullable = true)
    private Date exceptionDate;

    @Column(name = "exception_reason", nullable = true)
    private String exceptionReason;

    @Column(name = "sent_from", nullable = false)
    private String sentFrom;

    @Column(name = "processed_date", nullable = true)
    private Date processedDate;

}
