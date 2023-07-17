package com.data.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PieceDTO implements Serializable{

    private int id;

    private String name;

    private boolean isRed;

    private String image;

    private int currentCol;

    private int currentRow;

}
