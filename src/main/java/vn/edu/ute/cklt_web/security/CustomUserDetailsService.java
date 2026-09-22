package vn.edu.ute.cklt_web.security;

import vn.edu.ute.cklt_web.entity.User;
import vn.edu.ute.cklt_web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByMssv(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with MSSV: " + username));

        return new CustomUserDetails(user);
    }
}
