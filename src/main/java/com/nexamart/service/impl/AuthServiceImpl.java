package com.nexamart.service.impl;

import java.awt.List;
import java.net.Authenticator.RequestorType;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexamart.config.JwtProvider;
import com.nexamart.domain.USER_ROLE;
import com.nexamart.modal.Cart;
import com.nexamart.modal.Seller;
import com.nexamart.modal.User;
import com.nexamart.modal.VerificationCode;
import com.nexamart.repository.CartRepository;
import com.nexamart.repository.SellerRepository;
import com.nexamart.repository.UserRepository;
import com.nexamart.repository.VerificationCodeRepository;
import com.nexamart.request.LoginRequest;
import com.nexamart.response.AuthResponse;
import com.nexamart.response.SignUpRequest;
import com.nexamart.service.AuthService;
import com.nexamart.service.EmailService;
import com.nexamart.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final VerificationCodeRepository verificationCodeRepository;
	private final JwtProvider jwtProvider;
	private final EmailService emailService;
	private final CustomUserServiceImpl customUserServiceImpl;
	private final SellerRepository sellerRepository;

	@Override
	public void sentLoginOtp(String email, USER_ROLE role) throws Exception {

		String SIGNING_PREFIXS = "signing_";

		if (email.startsWith(SIGNING_PREFIXS)) {

			email = email.substring(SIGNING_PREFIXS.length());
			// Validate that user with this email exists

			if (role.equals(USER_ROLE.ROLE_SELLER)) {
				Seller seller = sellerRepository.findByEmail(email);
				if (seller == null) {
					throw new Exception("Seller not found");
				}

			} else {
				User user = userRepository.findByEmail(email);
				if (user == null) {
					throw new Exception("User not exist with provided email");
				}
			}

		}
		// Remove existing OTP record for this email (if any)

		VerificationCode isExist = verificationCodeRepository.findByEmail(email);

		if (isExist != null) {
			verificationCodeRepository.delete(isExist);
		}
		// Generate new OTP and save it

		String otp = OtpUtil.generateOtp();

		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(email);
		verificationCodeRepository.save(verificationCode);
		// Send OTP email
		String subject = "Nexamart login/signup otp";
		String text = "Your login/signup otp is: " + otp;

		emailService.sendVerificationOtpEmail(email, otp, subject, text);

	}

	@Override
	public String createUser(SignUpRequest req) throws Exception {
		// Validate OTP
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

		if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
			throw new Exception("Wrong otp"); // Check if error occur change to Exception
		}
		// Check if user already exists; if not, create a new user

		User user = userRepository.findByEmail(req.getEmail());

		if (user == null) {
			User createdUser = new User();

			createdUser.setEmail(req.getEmail());
			createdUser.setFullName(req.getFullname());
			createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
			createdUser.setMobile("8520852020");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

			user = userRepository.save(createdUser);
			// Create a cart for the new user

			Cart cart = new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
		}
		// Authenticate user and generate JWT token

		java.util.List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

		Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return jwtProvider.generateToken(authentication);
	}

	@Override
	public AuthResponse signing(LoginRequest req) {
		String username = req.getEmail();
		String otp = req.getOtp();

		Authentication authentication = authencate(username, otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMsg("Login sucess");

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
		authResponse.setRole(USER_ROLE.valueOf(roleName));
		return authResponse;
	}

	private Authentication authencate(String username, String otp) {
		UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
		
		String SELLER_PREFIX = "seller_";

		if (username.startsWith(SELLER_PREFIX)) {

			username = username.substring(SELLER_PREFIX.length());

		}

		if (userDetails == null) {
			throw new BadCredentialsException("Wrong Username");
		}

		VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

		if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			throw new BadCredentialsException("Wrong otp");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
