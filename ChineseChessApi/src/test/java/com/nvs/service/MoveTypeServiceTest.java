package com.nvs.service;

import com.nvs.service.impl.MoveTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor public class MoveTypeServiceTest{
   private final MoveTypeServiceImpl moveTypeService = new MoveTypeServiceImpl();

   @Test
   public void testIsHorizontallyMoving(){
      // Test cases
      assertTrue(moveTypeService.isHorizontallyMoving(3, 3));
      assertFalse(moveTypeService.isHorizontallyMoving(2, 3));
   }

   @Test
   public void testIsUpMoving(){
      // Test cases
      assertTrue(moveTypeService.isUpMoving(true, 4, 2)); // Red player moving up
      assertFalse(moveTypeService.isUpMoving(true, 2, 4)); // Red player moving down
      assertTrue(moveTypeService.isUpMoving(false, 2, 4)); // Black player moving up
      assertFalse(moveTypeService.isUpMoving(false, 4, 2)); // Black player moving down
   }

   @Test
   public void testIsVerticallyMoving(){
      // Test cases
      assertTrue(moveTypeService.isVerticallyMoving(2, 2));
      assertFalse(moveTypeService.isVerticallyMoving(2, 3));
   }
}
