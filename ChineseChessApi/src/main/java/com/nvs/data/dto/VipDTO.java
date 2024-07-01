package com.nvs.data.dto;

import com.nvs.common.Default;
import com.nvs.common.ErrorMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VipDTO extends AuditorDTO{

   private Integer id;

   @NotBlank(message = ErrorMessage.BLANK_DATA)
   private String name;

   @NotNull(message = ErrorMessage.NULL_DATA)
   @Min(value = Default.Game.DEPOSIT_MILESTONES, message = ErrorMessage.DEPOSIT_MILESTONES)
   private Integer depositMilestones;

}
