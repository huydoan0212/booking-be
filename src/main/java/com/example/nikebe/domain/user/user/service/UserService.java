package com.example.nikebe.domain.user.user.service;


import com.example.nikebe.common.OTPType;
import com.example.nikebe.common.Role;
import com.example.nikebe.common.UserStatus;
import com.example.nikebe.common.helper.InputValidator;
import com.example.nikebe.common.pagination.PageDto;
import com.example.nikebe.domain.mail_sms.mail.service.SendMailService;
import com.example.nikebe.domain.role.repository.RoleRepository;
import com.example.nikebe.domain.user.user.UserDetailsImplement;
import com.example.nikebe.domain.user.user.dto.*;
import com.example.nikebe.domain.user.user.entity.UserEntity;
import com.example.nikebe.domain.user.user.mapper.UserMapper;
import com.example.nikebe.domain.user.user.repository.UserRepository;
import com.example.nikebe.domain.user.userOtp.entity.UserOtpEntity;
import com.example.nikebe.domain.user.userOtp.repository.UserOtpRepository;
import com.example.nikebe.domain.user.userToken.entity.UserTokenEntity;
import com.example.nikebe.domain.user.userToken.repository.UserTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final UserOtpRepository userOtpRepository;
    private final SendMailService sendMailService;
    private final UserTokenRepository userTokenRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, UserMapper userMapper, UserOtpRepository userOtpRepository, SendMailService sendMailService, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
        this.modelMapper.addMappings(new PropertyMap<UpdateProfileDTO, UserEntity>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        this.modelMapper.addMappings(new PropertyMap<UpdateUserDto, UserEntity>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        this.modelMapper.addMappings(new PropertyMap<CreateUserDto, UserEntity>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        this.modelMapper.addMappings(new PropertyMap<RegisterUserDto, UserEntity>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        this.userOtpRepository = userOtpRepository;
        this.sendMailService = sendMailService;
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public UserResponseDto createByAdmin(CreateUserDto user) throws IOException, GeneralSecurityException {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        if (!user.getPassword().equals(user.getConfirmPassword()))
            throw new BadRequestException("Both of password and confirm password are must equal", new Throwable("password and confirmPassword"));
        userEntity.setUserStatus(UserStatus.ACTIVATE);
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setIdNumber(user.getIdNumber());
        userEntity.setUserRole(roleRepository.findByRoleReference(user.getRole().toString()));
        userEntity = (userRepository.save(userEntity));
        return userMapper.userToUserResponseDto(userEntity);
    }

    @Override
    public UserResponseDto updateUserByAdmin(UUID id, UpdateUserDto user) throws BadRequestException {
        UserEntity userUpdate = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Can not get the user", new Throwable("id")));
        modelMapper.map(user, userUpdate);
        userUpdate = userRepository.save(userUpdate);
        return userMapper.userToUserResponseDto(userUpdate);
    }

    @Override
    public List<UserResponseDto> getAllUser() {
        return userRepository.findAll().stream().map(userMapper::userToUserResponseDto).toList();
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        return userMapper.userToUserResponseDto(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found", new Throwable("id"))));
    }

    @Override
    public UserResponseDto getProfileMe() {
        UserEntity userUpdate = ((UserDetailsImplement) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return userMapper.userToUserResponseDto(userUpdate);
    }

    @Override
    public PageDto<UserResponseDto> filterUser(Specification<UserEntity> specification, Pageable pageable) {
        // Lấy ra trang dữ liệu của UserEntity
        Page<UserEntity> userPage = userRepository.findAll(specification, pageable);
        // Chuyển đổi từng UserEntity sang UserResponseDto
        Page<UserResponseDto> dtoPage = userPage.map(userMapper::userToUserResponseDto);
        // Sử dụng constructor của PageDto nhận đối tượng Page<T>
        return new PageDto<>(dtoPage);
    }


    @Override
    public UserResponseDto updateProfileUser(UpdateProfileDTO user) {
        UserEntity userUpdate = ((UserDetailsImplement) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        modelMapper.map(user, userUpdate);
        userUpdate = userRepository.save(userUpdate);
        return userMapper.userToUserResponseDto(userUpdate);
    }

    @Override
    public Boolean deleteUser(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable("id")));
        userRepository.delete(user);
        return true;
    }

    @Override
    public void forgotPassword(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable("username")));
        userOtpRepository.deleteOtpsByUserIdAndType(user.getId(), OTPType.FORGOT_PASSWORD);
        sendOtp(user, OTPType.FORGOT_PASSWORD);
    }

    @Override
    public Object checkOtp(String username, String otp, OTPType otpType) throws BadRequestException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable("username")));
        UserOtpEntity userOtpEntity = userOtpRepository.findForgotOtpByUserId(user.getId(), otpType)
                .orElseThrow(() -> new RuntimeException("Otp not found", new Throwable("otp")));
        if (userOtpEntity.getOtpExpiredTime().isBefore(OffsetDateTime.now()))
            throw new BadRequestException("Otp is expired", new Throwable("otp"));
        if (!bCryptPasswordEncoder.matches(otp, userOtpEntity.getOtpCode()))
            throw new BadRequestException("Otp is invalid", new Throwable("otp"));
        if (userOtpEntity.getUsed()) throw new BadRequestException("Otp is used", new Throwable("otp"));
        if (otpType.equals(OTPType.FORGOT_PASSWORD)) {
            userOtpRepository.delete(userOtpEntity);
            String token = UUID.randomUUID().toString();
            UserTokenEntity userTokenEntity = new UserTokenEntity(token, OffsetDateTime.now().plusMinutes(5), false, user);
            userTokenRepository.save(userTokenEntity);
            return new TokenResponseDto(token);
        }
        if (otpType.equals(OTPType.REGISTER)) {
            user.setUserStatus(UserStatus.ACTIVATE);
            userRepository.save(user);
            userOtpRepository.delete(userOtpEntity);
            return Boolean.TRUE; // Trả về true khi kích hoạt thành công
        }
        return null;
    }


    @Override
    public Boolean resetPassword(ResetPasswordDto dto) throws BadRequestException {
        UserTokenEntity userTokenEntity = userTokenRepository.findByToken(dto.getToken()).orElseThrow(() -> new EntityNotFoundException("Token not found", new Exception("token")));
        if (userTokenEntity.getExpiredAt().isAfter(OffsetDateTime.now())) {
            if (dto.getPassword().equals(dto.getConfirmPassword())) {
                UserEntity user = userTokenEntity.getUser();
                user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
                userRepository.save(user);
                userTokenRepository.delete(userTokenEntity);
                return true;
            }
            throw new BadRequestException("Both of password and confirm password are must equal", new Throwable("password and confirmPassword"));
        }
        throw new BadRequestException("Token is expired", new Throwable("token"));
    }

    @Override
    public Boolean register(RegisterUserDto dto) throws BadRequestException {
        UserEntity existingUser = userRepository.findByUsername(dto.getUsername()).orElse(null);
        if (existingUser != null) {
            if (existingUser.getUserStatus().equals(UserStatus.NOT_ACTIVATED)) {
                throw new BadRequestException("User is not activated", new Throwable("username"));
            } else {
                throw new BadRequestException("User already exists", new Throwable("username"));
            }
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password must be equal", new Throwable("password and confirmPassword"));
        }
        UserEntity newUser = modelMapper.map(dto, UserEntity.class);
        newUser.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        newUser.setUserRole(roleRepository.findByRoleReference(Role.USER.toString()));
        newUser.setUserStatus(UserStatus.NOT_ACTIVATED);
        newUser = userRepository.save(newUser);
        sendOtp(newUser, OTPType.REGISTER);
        return true;
    }

    @Override
    public Boolean activeProfile(UserLoginDto dto) throws BadRequestException {
        UserEntity user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found", new Throwable("username")));
        if (bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            userOtpRepository.deleteOtpsByUserIdAndType(user.getId(), OTPType.REGISTER);
            sendOtp(user, OTPType.REGISTER);
            return true;
        }
        throw new BadRequestException("Password is not correct", new Throwable("password"));
    }

    private String generateOtp() {
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(otp);
    }

    private void sendOtp(UserEntity user, OTPType otpType) {
        String otp = generateOtp();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime expiredTime = now.plusMinutes(5);
        UserOtpEntity userOtpEntity = new UserOtpEntity(bCryptPasswordEncoder.encode(otp), expiredTime, false, otpType, user);
        userOtpRepository.save(userOtpEntity);
        if (InputValidator.checkInputType(user.getUsername()).equals("email")) {
            sendMailService.sendEmail(user.getUsername(), "Mã xác nhận", "Mã xác nhận của bạn là: " + otp);
        } else {
            // send sms
        }

    }

}
