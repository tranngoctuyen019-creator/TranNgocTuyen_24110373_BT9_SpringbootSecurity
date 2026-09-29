package vn.iotstar.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VerifyOtpDTO {
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6, max = 6, message = "OTP gồm 6 chữ số")
    private String otp;
}
