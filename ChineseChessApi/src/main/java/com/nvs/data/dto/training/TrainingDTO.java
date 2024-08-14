package com.nvs.data.dto.training;

import com.nvs.data.dto.AuditorDTO;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TrainingDTO extends AuditorDTO {

  private Long id;

  @NotBlank(message = "BLANK_DATA")
  private String title;

  private Long parentTrainingId;

  private List<TrainingDTO> childTrainingDTOs;

}
