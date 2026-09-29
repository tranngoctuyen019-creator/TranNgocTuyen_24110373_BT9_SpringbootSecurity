package vn.iotstar.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDTO {
    private Long id;
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;
    private String description;
    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải >= 0")
    private BigDecimal price;
    private String imageUrl;
    private Long userId;
    private String username;
    private MultipartFile image;
}
