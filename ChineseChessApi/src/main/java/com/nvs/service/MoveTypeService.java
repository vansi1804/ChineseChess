package com.nvs.service;

public interface MoveTypeService{

   boolean isHorizontallyMoving(int fromRow, int toRow);

   boolean isUpMoving(boolean isRed, int fromRow, int toRow);

   boolean isVerticallyMoving(int fromCol, int toCol);

}
