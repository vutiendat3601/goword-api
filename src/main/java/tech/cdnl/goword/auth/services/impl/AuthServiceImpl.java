package tech.cdnl.goword.auth.services.impl;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import tech.cdnl.goword.auth.models.dto.SessionDto;
import tech.cdnl.goword.auth.models.dto.TokenDto;
import tech.cdnl.goword.auth.models.entity.RolePermission;
import tech.cdnl.goword.auth.models.entity.Session;
import tech.cdnl.goword.auth.models.entity.UserRole;
import tech.cdnl.goword.auth.models.request.SignUpRequest;
import tech.cdnl.goword.auth.repos.RolePermissionRepo;
import tech.cdnl.goword.auth.repos.SessionRepo;
import tech.cdnl.goword.auth.repos.UserRoleRepo;
import tech.cdnl.goword.auth.services.AuthService;
import tech.cdnl.goword.auth.services.AuthServiceAsync;
import tech.cdnl.goword.exceptions.AppErrorCode;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.exceptions.ResourceConflictException;
import tech.cdnl.goword.exceptions.ResourceNotFoundException;
import tech.cdnl.goword.shared.AppConstant;
import tech.cdnl.goword.shared.utils.JwtUtil;
import tech.cdnl.goword.user.models.dto.UserDto;
import tech.cdnl.goword.user.models.entity.User;
import tech.cdnl.goword.user.repos.UserRepo;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
	@Value("${app.refresh_token.valid_time_sec}")
	private long refTokenValidTimeSec;

	@Value("${app.name}")
	private String appName;

	@Value("${app.website}")
	private String appWebsite;

	@Value("${spring.mail.username}")
	private String serverEmailAddress;

	@Value("${app.auth.verification_code.valid_time_sec}")
	private long verifCodeValidTimeSec;

	private final AuthServiceAsync authServiceAsync;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passEncoder;
	private final UserRepo userRepo;
	private final UserRoleRepo userRoleRepo;
	private final SessionRepo sessionRepo;
	private final Base64.Encoder base64Encoder;
	private final RolePermissionRepo rolePermissionRepo;

	@Override
	public void signUp(SignUpRequest signUpReq) {
		if (userRepo.existsByEmail(signUpReq.getEmail())) {
			throw new ResourceConflictException(
					AppErrorMessage.EMAIL_DUPLICATED,
					AppErrorCode.RESOURCE_CONFLICT_EMAIL_DUPLICATED);
		}
		User user = new User(
				signUpReq.getFirstName(),
				signUpReq.getLastName(),
				signUpReq.getEmail(),
				passEncoder.encode(signUpReq.getPassword()));
		user = userRepo.save(user);
		UserRole userRole = new UserRole(user.getId(), "USER", true);
		userRoleRepo.save(userRole);
		authServiceAsync.sendEmailVerificationCode(user.getEmail());
	}

	@Validated
	@Override
	public TokenDto generateToken(SessionDto sessionDto) {
		UserDto userDto = sessionDto.getUser();
		Map<String, Object> claims = new LinkedHashMap<>();
		claims.put("sub", userDto.getId());
		claims.put("session", sessionDto.getId());
		claims.put("email", userDto.getEmail());
		Collection<GrantedAuthority> grantedAuthorities = getAllAuthorities(userDto);
		Set<String> authorities = grantedAuthorities.stream().map(authority -> authority.getAuthority())
				.collect(Collectors.toSet());
		claims.put("acl", authorities);
		String acccessToken = jwtUtil.generateJwt(claims);
		String refToken = AppConstant.REFRESH_TOKEN_PREFIX
				+ RandomString.make(AppConstant.REFRESH_TOKEN_RANDOM_LENGTH);

		Session session = sessionRepo.findById(sessionDto.getId())
				.orElseThrow(() -> new RuntimeException());
		session.setRefreshToken(base64Encoder.encodeToString(refToken.getBytes()));
		session.setRefreshTokenExpireAt(ZonedDateTime.now().toEpochSecond() + refTokenValidTimeSec);
		sessionRepo.save(session);
		userDto.setRoles(authorities.stream().filter(authority -> authority.startsWith("ROLE_"))
				.map(role -> role.replaceAll("ROLE_", "")).collect(Collectors.toSet()));
		userDto.setPermissions(
				authorities.stream().filter(authority -> !authority.startsWith("ROLE_"))
						.collect(Collectors.toSet()));
		return new TokenDto(
				acccessToken,
				refToken,
				session.getId(),
				userDto);
	}

	@Override
	public void verifyEmail(String verifCode) {
		verifCode = base64Encoder.encodeToString(verifCode.getBytes());
		User user = userRepo.findByVerificationCode(verifCode)
				.orElseThrow(() -> new ResourceNotFoundException(
						AppErrorMessage.VERIFICATION_CODE_INVALID));
		if (ZonedDateTime.now().toEpochSecond() < user.getVerificationCodeExpireAt()) {
			user.setVerified(true);
			user.setVerificationCode(null);
			user.setVerificationCodeExpireAt(null);
			user = userRepo.save(user);
		}
	}

	@Override
	public Collection<GrantedAuthority> getAllAuthorities(UserDto userDto) {
		final Collection<GrantedAuthority> authorities = new HashSet<>();
		List<UserRole> userRoles = userRoleRepo.findAllByUserId(userDto.getId());
		userRoles.forEach(ur -> authorities.add(new SimpleGrantedAuthority("ROLE_" + ur.getRoleName())));

		List<RolePermission> rolePermissions = rolePermissionRepo
				.findAllByRoleNames(userRoles.stream().map(ur -> ur.getRoleName()).toList());
		rolePermissions.forEach(rp -> authorities.add(new SimpleGrantedAuthority(rp.getPermissionName())));
		return authorities;
	}

	@Override
	public void signOut(UUID sessionId) {
		Session session = sessionRepo.findById(sessionId)
				.orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.SESSION_NOT_FOUND));
		session.setSignedInAt(ZonedDateTime.now());
		session.setRefreshToken(null);
		session.setRefreshTokenExpireAt(null);
		sessionRepo.save(session);
	}

}
