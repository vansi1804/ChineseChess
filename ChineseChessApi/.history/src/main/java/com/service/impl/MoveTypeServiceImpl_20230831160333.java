package com.service.impl;

import org.springframework.stereotype.Service;

import com.service.MoveTypeService;

@Service
public class MoveTypeServiceImpl implements MoveTypeService {

    @Override
    public boolean isHorizontallyMoving(int fromRow, int toRow) {
        return fromRow == toRow;
    }

    @Override
    public boolean isUpMoving(boolean isRed, int fromRow, int toRow) {
        return (isRed && (fromRow > toRow)) || (!isRed && (fromRow < toRow));
    }

    @Override
    public boolean isVerticallyMoving(int fromCol, int toCol) {
        return fromCol == toCol;
    }

}
