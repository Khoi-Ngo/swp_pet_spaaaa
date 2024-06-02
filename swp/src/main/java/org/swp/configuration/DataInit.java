package org.swp.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.UserRole;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInit {

    @Bean
    public CommandLineRunner initData(IUserRepository IUserRepository, IShopRepository iShopRepository) {
        return args -> {
            // Check if there's any existing data in the database
            if (IUserRepository.count() == 0) {
                // Create some initial users
                List<User> initialUsers = Arrays.asList(
                        createUser("admin", "admin", UserRole.ADMIN, "Admin", "User", "admin@example.com"),
                        createUser("customer", "customer", UserRole.CUSTOMER, "Customer", "User", "customer@example.com"),
                        createUser("shop_owner", "shop_owner", UserRole.SHOP_OWNER, "Shop", "Owner", "owner@example.com")
                );

                // Save the initial users to the database
                IUserRepository.saveAll(initialUsers);
            }
        };
    }

    private User createUser(String username, String password, UserRole role, String firstName, String lastName, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }

}
