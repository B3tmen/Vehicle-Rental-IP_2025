package org.unibl.etf.carrentalbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.exception.UnauthorizedException;
import org.unibl.etf.carrentalbackend.model.entities.User;
import org.unibl.etf.carrentalbackend.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            User user = userRepository.findByUsername(username);
            if(user == null) {
                throw new UnauthorizedException("User not found with username: " + username);
            }
            else{
                return new UserPrincipal(user);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
