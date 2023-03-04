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
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "role_id")
    private long roleId;
    @Column(name = "name")
    private String name;
    @Column(name = "avata")
    private String avata;
    @Column(name = "elo_score")
    private long eloScore;
    @Column(name = "level_id")
    private long levelId;
}
