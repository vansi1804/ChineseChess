package com.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "matches")
public class Match implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "time", updatable = false)
    private int time;

    @Column(name = "moving_time", updatable = false)
    private int movingTime;

    @Column(name = "cumulative_time", updatable = false)
    private int cumulativeTime;

    @Column(name = "bet", updatable = false)
    private int bet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1_id", updatable = false)
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2_id", updatable = false)
    private Player player2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", updatable = false)
    private Training training;

    @Column(name = "result")
    private Long result;

    @CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_at", updatable = false)
	private Date startAt;

    @LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "stop_at", updatable = false)
	private Date stopAt;

}
