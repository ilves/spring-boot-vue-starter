package ee.golive.services;

import ee.golive.controllers.api.models.user.*;
import ee.golive.entity.User;
import ee.golive.model.Principal;
import ee.golive.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class UserServiceImpl implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ModelMapper modelMapper;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public Page<UserResponse> findAll(int page, int size) {
    Pageable pageable = new PageRequest(page-1, size);
    return userRepository.findAll(pageable).map(x -> modelMapper.map(x, UserResponse.class));
  }

  public UserResponse findById(Long id) {
    User user = userRepository.findOne(id);
    return user != null ? modelMapper.map(user, UserResponse.class) : null;
  }

  public UserResponse findByEmail(String email) {
    User user = userRepository.findUserByEmail(email);
    return user != null ? modelMapper.map(user, UserResponse.class) : null;
  }

  @Transactional
  public UserResponse create(CreateUser userData) {
    User user = modelMapper.map(userData, User.class);
    user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
    userRepository.save(user);
    return modelMapper.map(user, UserResponse.class);
  }

  @Transactional
  public UserResponse register(RegisterUser userData) {
    User user = modelMapper.map(userData, User.class);
    user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
    userRepository.save(user);
    return modelMapper.map(user, UserResponse.class);
  }

  @Transactional
  public UserResponse update(Long id, UpdateUser userData) {
    User user = userRepository.findOne(id);
    mapCommon(userData, user);
    if (userData.getPassword() != null) {
      user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
    }
    userRepository.save(user);
    return modelMapper.map(user, UserResponse.class);
  }

  @Transactional
  public void delete(Long id) {
    User user = userRepository.findOne(id);
    userRepository.delete(user);
  }

  @Transactional
  public void updateLastLogin(Long id, Date date) {
    User user = userRepository.findOne(id);
    user.setLastLogin(date);
    userRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }
    return new Principal(user);
  }

  public UserDetails loadByUserId(Long id) {
    User user = userRepository.findOne(id);
    return new Principal(user);
  }

  public UserResponse convertToDTO(User user) {
    return modelMapper.map(user, UserResponse.class);
  }

  public void mapCommon(UserCommon userData, User user) {
    user.setName(userData.getName());
    user.setEmail(userData.getEmail());
  }
}
