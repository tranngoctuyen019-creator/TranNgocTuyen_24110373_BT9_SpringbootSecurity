package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.UserDTO;

public interface UserService {

    Page<UserDTO> findAll(
            String keyword,
            int page,
            int size
    );

    UserDTO findById(Long id);

    UserDTO create(
            UserDTO dto,
            MultipartFile image
    );

    UserDTO update(
            Long id,
            UserDTO dto,
            MultipartFile image
    );

    void delete(Long id);

    long countUsers();

    long countProducts(Long userId);
}