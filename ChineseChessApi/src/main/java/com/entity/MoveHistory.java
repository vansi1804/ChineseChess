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
@Table(name = "move_history")
public class MoveHistory {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "game_id")
    private long gameId;
    @Column(name = "turn")
    private long turn;
    @Column(name = "player_id")
    private long playerId;
    @Column(name = "piece_id")
    private int pieceId;
    @Column(name = "from_col")
    private int fromCol;
    @Column(name = "fromRow")
    private int from_row;
    @Column(name = "toCol")
    private int to_col;
    @Column(name = "toRow")
    private int to_row;
}
