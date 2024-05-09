package com.nvs.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "pieces")
public class Piece implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name = "name", updatable = false)
    private String name;

    @Column(name = "is_red", updatable = false)
    private boolean isRed;

    @Column(name = "image")
    private String image;

    @Column(name = "start_col", updatable = false)
    private int currentCol;

    @Column(name = "start_row", updatable = false)
    private int currentRow;
}
