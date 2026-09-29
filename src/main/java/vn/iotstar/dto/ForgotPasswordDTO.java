package vn.iotstar.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ForgotPasswordDTO {
    @NotBlank @Email
    private String email;
}
