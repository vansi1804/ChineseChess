package com.nvs.data.dto;

import com.nvs.common.Default;
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
public class RankDTO extends AuditorDTO {

  private Integer id;

  @NotBlank(message = "BLANK_DATA")
  private String name;

  @NotNull(message = "NULL_DATA")
  @Min(value = Default.Game.ELO_MILESTONES, message = "ELO_MILESTONES")
  private Integer eloMilestones;

}
