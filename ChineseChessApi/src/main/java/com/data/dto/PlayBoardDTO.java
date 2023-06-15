package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayBoardDTO {
    private PieceDTO[][] state;

    public void print(PieceDTO pieceDTO) {
        System.out.println("===========================================");
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 9; col++) {
                String s = " +   ";
                if ((pieceDTO != null) && (col == pieceDTO.getCurrentCol() && row == pieceDTO.getCurrentRow())) {
                    s = "[  ] ";
                }

                if (state[col - 1][row - 1] == null) {
                    System.out.printf(s);
                } else {
                    String p = state[col - 1][row - 1].getName().charAt(0)
                            + (state[col - 1][row - 1].isRed() ? "1" : "2");
                    if (pieceDTO != null && state[col - 1][row - 1].getId() == pieceDTO.getId()) {
                        p = "[" + p + "] ";
                    } else {
                        p = " " + p + "  ";
                    }
                    System.out.printf(p);
                }
            }
            System.out.println("\n");
        }
        System.out.println("===========================================");
    }
}
