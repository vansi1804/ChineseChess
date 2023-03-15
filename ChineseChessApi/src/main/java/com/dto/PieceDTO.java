package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PieceDTO {
    private int id;
    private String name;
    private boolean isRed;
    private String image;
    private int startCol;
    private int startRow;
}
