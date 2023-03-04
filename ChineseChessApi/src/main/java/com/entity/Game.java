package com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "time")
	private String time;
    @Column(name = "player1_id")
	private long player1Id;
    @Column(name = "player2_id")
	private long player2Id;
    @Column(name = "result")
	private long result;
}
