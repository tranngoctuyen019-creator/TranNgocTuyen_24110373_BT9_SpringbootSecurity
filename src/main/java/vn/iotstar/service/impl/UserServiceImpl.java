package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(
            String keyword,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "id")
        );

        return userRepository
                .search(keyword == null ? "" : keyword, pageable)
                .map(user -> {

                    UserDTO dto = mapper.toDTO(user);

                    dto.setProductCount(
                            userRepository.countProductsByUserId(user.getId())
                    );

                    return dto;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User không tồn tại"
                        )
                );

        UserDTO dto = mapper.toDTO(user);

        dto.setProductCount(
                userRepository.countProductsByUserId(id)
        );

        return dto;
    }

    @Override
    @Transactional
    public UserDTO create(
            UserDTO dto,
            MultipartFile image) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException(
                    "Username đã tồn tại"
            );
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Email đã tồn tại"
            );
        }

        User user = mapper.toEntity(dto);

        String roleName =
                dto.getRoleName() == null ||
                dto.getRoleName().isBlank()
                        ? "ROLE_USER"
                        : dto.getRoleName();

        Role role = roleRepository
                .findByName(roleName)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Role không tồn tại"
                        )
                );

        user.setRole(role);

        user.setPassword(
                passwordEncoder.encode("123456")
        );

        user.setEnabled(dto.isEnabled());
        if (image != null && !image.isEmpty()) {

            CloudinaryUploadResult result =
                    cloudinaryService.uploadUserImage(image);

            user.setImages(
                    result.url() + "|" + result.publicId()
            );
        }

        return mapper.toDTO(
                userRepository.save(user)
        );
    }

    @Override
    @Transactional
    public UserDTO update(
            Long id,
            UserDTO dto,
            MultipartFile image) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User không tồn tại"
                        )
                );

        if (!user.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {

            throw new IllegalArgumentException(
                    "Username đã tồn tại"
            );
        }

        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {

            throw new IllegalArgumentException(
                    "Email đã tồn tại"
            );
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setEnabled(dto.isEnabled());

        if (dto.getRoleName() != null
                && !dto.getRoleName().isBlank()) {

            Role role = roleRepository
                    .findByName(dto.getRoleName())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Role không tồn tại"
                            )
                    );

            user.setRole(role);
        }

        // Chỉ thay ảnh khi người dùng chọn ảnh mới
        if (image != null && !image.isEmpty()) {

            String oldImage = user.getImages();

            if (oldImage != null && oldImage.contains("|")) {

                String oldPublicId =
                        oldImage.substring(
                                oldImage.indexOf('|') + 1
                        );

                cloudinaryService.delete(oldPublicId);
            }

            CloudinaryUploadResult result =
                    cloudinaryService.uploadUserImage(image);

            user.setImages(
                    result.url() + "|" + result.publicId()
            );
        }

        return mapper.toDTO(
                userRepository.save(user)
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User không tồn tại"
                        )
                );

        if (user.getImages() != null
                && user.getImages().contains("|")) {

            String publicId =
                    user.getImages().substring(
                            user.getImages().indexOf('|') + 1
                    );

            cloudinaryService.delete(publicId);
        }

        userRepository.delete(user);
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public long countProducts(Long userId) {
        return userRepository.countProductsByUserId(userId);
    }
}