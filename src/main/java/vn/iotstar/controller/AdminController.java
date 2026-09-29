package vn.iotstar.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.UserRepository;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@GetMapping
	public String dashboard(Model model) {
		model.addAttribute("users", userRepository.findAll().stream().map(userMapper::toDTO).toList());
		return "admin/dashboard";
	}
}
